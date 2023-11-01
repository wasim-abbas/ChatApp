package com.wasim.csdl.patient_chatapp.views.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseActivity
import com.wasim.csdl.patient_chatapp.databinding.ActivityMessageChatBinding
import com.wasim.csdl.patient_chatapp.models.Chat
import com.wasim.csdl.patient_chatapp.models.Users
import com.wasim.csdl.patient_chatapp.notifications.Client
import com.wasim.csdl.patient_chatapp.notifications.Data
import com.wasim.csdl.patient_chatapp.notifications.MyResponse
import com.wasim.csdl.patient_chatapp.notifications.Sender
import com.wasim.csdl.patient_chatapp.notifications.Token
import com.wasim.csdl.patient_chatapp.utils.USER_ID
import com.wasim.csdl.patient_chatapp.utils.singleClick
import com.wasim.csdl.patient_chatapp.views.adapters.ChatsAdapter
import com.wasim.csdl.patient_chatapp.notifications.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageChatActivity : BaseActivity() {

    private val binding: ActivityMessageChatBinding by lazy {
        ActivityMessageChatBinding.inflate(layoutInflater)
    }
    var firebaseUser: FirebaseUser? = null
    var userIdVisit: String? = ""
    var chatsAdapter: ChatsAdapter? = null

    var mchatList: List<Chat>? = null
    var refrence: DatabaseReference? = null
    var notify = false

    lateinit var apiService: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        setSupportActionBar(binding.toolbarMessageChat)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.toolbarMessageChat.setNavigationOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
            finish()
        }

        apiService = Client.Client.getClient("https://fcm.googleapis.com/")!!.create(
            APIService::class.java)
        intent = intent
        userIdVisit = intent.getStringExtra(USER_ID)

        Log.i("UserAdapter", "User ID: Message Activity ${userIdVisit}")
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (userIdVisit != null) {
            refrence = FirebaseDatabase.getInstance().reference.child("Users").child(userIdVisit!!)

        refrence!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue(Users::class.java)

                    if (user != null) {
                        binding.userNameChatActivity.text = user.user_Name
                        Picasso.get().load(user.getProfile()).into(binding.prfileMchatimg)

                        retrieveMessages(firebaseUser!!.uid, userIdVisit!!, user.getProfile())
                    } else {
                        binding.userNameChatActivity.text = "No Name"
                        binding.prfileMchatimg.setImageResource(R.drawable.profile)
                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.message)
            }
        })

            seenMessage(userIdVisit!!)
        }else{
            showToast("User ID is null")
        }

        binding.sendMessagebtn.singleClick {
            notify = true
            val msg = binding.textMsg.text.toString()
            if (msg == "") {
                showToast("Please write message")
            } else {
                sendMessage(firebaseUser!!.uid, userIdVisit, msg)
            }
        }

        binding.attachImgFilebtn.singleClick {
            notify = true
            imagePicker.launch("image/*")
        }


    }


    val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) {

        // Handle the selected image here
        val loadingBar =
            ProgressDialog(this) // 'this' refers to the current Activity or Context
        loadingBar.setMessage("Please wait, image is sending...")
        loadingBar.show()

        val data: Intent? = Uri.parse(it.toString())?.let { it1 ->
            Intent(Intent.ACTION_PICK, it1)
        }

        val storageRefrece =
            FirebaseStorage.getInstance().reference.child("Chat Images")
        val ref = FirebaseDatabase.getInstance().reference
        val messageID = ref.push().key
        val filePath = storageRefrece.child("$messageID.jpg")

        var uploadTask: StorageTask<*>
        uploadTask = filePath.putFile(data!!.data!!)

        uploadTask.continueWithTask<Uri?>(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation filePath.downloadUrl
        }).addOnCompleteListener { task ->

            val downloadUrl = task.result
            val url = downloadUrl.toString()

            val messageHashMap = HashMap<String, Any?>()
            messageHashMap["sender"] = firebaseUser!!.uid
            messageHashMap["message"] = "sent you an image."
            messageHashMap["receiver"] = userIdVisit
            messageHashMap["isSeen"] = false
            messageHashMap["url"] = url
            messageHashMap["messageID"] = messageID

            ref.child("Chats").child(messageID!!).setValue(messageHashMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        loadingBar.dismiss()

                        //// add message notification using firebase cloud messaging
                        val refrence = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(firebaseUser!!.uid)

                        refrence.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(po: DataSnapshot) {
                                val user = po.getValue(Users::class.java)

                                if (notify) {
                                    sendNotification(userIdVisit, user!!.getUsername(), "sent you an image.")
                                }
                                notify = false
                            }

                            override fun onCancelled(error: DatabaseError) {
                                showToast(error.message)
                            }
                        })

                        showToast("Message Sent")
                    } else {
                        loadingBar.dismiss()
                        showToast("Error")
                    }
                }

        }
    }


    private fun sendMessage(senderID: String, receiverID: String?, msg: String) {

        val refrefece = FirebaseDatabase.getInstance().reference
        val messageKey = refrefece.push().key

        val messageHashMap = HashMap<String, Any?>()
        messageHashMap["sender"] = senderID
        messageHashMap["message"] = msg
        messageHashMap["receiver"] = receiverID
        messageHashMap["isSeen"] = false
        messageHashMap["url"] = ""
        messageHashMap["messageID"] = messageKey

        refrefece.child("Chats").child(messageKey!!).setValue(messageHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val chatsListRefrence =
                        FirebaseDatabase.getInstance().reference.child("ChatList")
                            .child(firebaseUser!!.uid)
                            .child(userIdVisit!!)

                    chatsListRefrence.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                chatsListRefrence.child("id").setValue(userIdVisit)
                            }
                            /// add receiver id in chat list
                            val chatsListReceiverRefrence =
                                FirebaseDatabase.getInstance().reference.child("ChatList")
                                    .child(userIdVisit!!)
                                    .child(firebaseUser!!.uid)
                            chatsListReceiverRefrence.child("id").setValue(firebaseUser!!.uid)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            showToast(error.message)
                        }
                    })

                    /// add sender id in chat list
                    chatsListRefrence.child("id").setValue(firebaseUser!!.uid)

                    showToast("Message Sent")
                } else {
                    showToast("Message not Sent")
                }
                binding.textMsg.setText("")
            }

        //// add message notification using firebase cloud messaging
        val userRefrence = FirebaseDatabase.getInstance().reference.child("Users")
            .child(firebaseUser!!.uid)

        userRefrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(po: DataSnapshot) {
                val user = po.getValue(Users::class.java)

                if (notify) {
                    sendNotification(receiverID, user!!.getUsername(), msg)
                }
                notify = false
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.message)
            }
        })

    }

    private fun sendNotification(receiverID: String?, userName: String, msg: String) {

        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
        val query = ref.orderByKey().equalTo(receiverID)

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(po: DataSnapshot) {

                for (dataSnapShot in po.children){

                    val token : Token? = dataSnapShot.getValue(Token::class.java)
                    val data = Data(
                        firebaseUser!!.uid,
                        R.mipmap.ic_launcher,
                        "$userName: $msg",
                        "New Message",
                        userIdVisit!!
                    )

                    val sender = Sender(data!!, token!!.getToken().toString())

                    apiService.sendNotification(sender)
                        .enqueue(object : Callback<MyResponse>
                        {
                        override fun onResponse(
                            call: Call<MyResponse>,
                            response: Response<MyResponse>
                        ) {
                            if (response.code() == 200){
                                if (response.body()!!.success != 1){
                                    showToast("Failed, Nothing happen")
                                }
                            }
                        }

                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            showToast(t.message)
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun retrieveMessages(senderId: String, receiverID: String, receiverImgURl: String) {

        mchatList = ArrayList()
        val refrence = FirebaseDatabase.getInstance().reference.child("Chats")

        refrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                (mchatList as ArrayList<Chat>).clear()
                for (po in snapshot.children) {
                    val chat = po.getValue(Chat::class.java)

                    if (chat!!.getReceiver().equals(senderId) && chat.getSender()
                            .equals(receiverID) ||
                        chat.getReceiver().equals(receiverID) && chat.getSender().equals(senderId)
                    ) {
                        (mchatList as ArrayList<Chat>).add(chat)
                    }
                    chatsAdapter = ChatsAdapter(
                        this@MessageChatActivity,
                        (mchatList as ArrayList<Chat>),
                        receiverImgURl
                    )
                    binding.rvChat.setHasFixedSize(true)
                    binding.rvChat.adapter = chatsAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.message)
            }

        })

    }

    var seenListner: ValueEventListener? = null

    private fun seenMessage(userID: String) {

        val refrence = FirebaseDatabase.getInstance().reference.child("Chats")

        seenListner = refrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(po: DataSnapshot) {
                for (dataSnapshot in po.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)

                    if (chat!!.getReceiver().equals(firebaseUser!!.uid) && chat.getSender()
                            .equals(userID)
                    ) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["isSeen"] = true
                        dataSnapshot.ref.updateChildren(hashMap)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.message)
            }

        })

    }

    override fun onPause() {
        super.onPause()
        refrence!!.removeEventListener(seenListner!!)
    }

    override fun attachViewMode() {}
}