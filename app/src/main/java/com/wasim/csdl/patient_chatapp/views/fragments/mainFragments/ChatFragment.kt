package com.wasim.csdl.patient_chatapp.views.fragments.mainFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.wasim.csdl.patient_chatapp.base.BaseFragments
import com.wasim.csdl.patient_chatapp.databinding.FragmentChatBinding
import com.wasim.csdl.patient_chatapp.models.ChatList
import com.wasim.csdl.patient_chatapp.models.Users
import com.wasim.csdl.patient_chatapp.viewModels.BaseViewModel
import com.wasim.csdl.patient_chatapp.views.adapters.UserAdapter


class ChatFragment : BaseFragments<BaseViewModel>() {
    override val viewModelClass: Class<BaseViewModel>
        get() = BaseViewModel::class.java

    private lateinit var binding: FragmentChatBinding
    private lateinit var userAdapter: UserAdapter
    private var mUser: ArrayList<Users>? = null
    private var userChatIist: ArrayList<ChatList>? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun observeLiveData() {}

    override fun init() {

        firebaseUser = FirebaseAuth.getInstance().currentUser

        userChatIist = ArrayList()


        val ref = FirebaseDatabase.getInstance().reference.child("ChatList")
            .child(firebaseUser!!.uid)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (userChatIist as ArrayList).clear()

                for (dataSnapshot in snapshot.children) {
                    val chatList = dataSnapshot.getValue(ChatList::class.java)
                    (userChatIist as ArrayList).add(chatList!!)
                }
                retriveChatList()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result

                Log.d("Refreshed token1", token)
                if (firebaseUser != null) {
                    updateToken(token)
                }

            }
        }

    }

    private fun updateToken(token: String) {

        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
        val token1 = com.wasim.csdl.patient_chatapp.notifications.Token(token)
        ref.child(firebaseUser!!.uid).setValue(token1)

    }



    private fun retriveChatList() {

        mUser = ArrayList()

        val ref = FirebaseDatabase.getInstance().reference.child("Users")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUser as ArrayList).clear()

                for (dataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(Users::class.java)

                    for (eachChatList in userChatIist!!) {
                        if (user!!.getUSerId().equals(eachChatList.getId())) {
                            (mUser as ArrayList).add(user!!)
                        }

                    }
                }
                userAdapter = UserAdapter(currentActivity(), true)
                userAdapter.updateData(mUser!!)
                binding.RecycviewChatList.adapter = userAdapter

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}