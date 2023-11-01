package com.wasim.csdl.patient_chatapp.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseAdapter
import com.wasim.csdl.patient_chatapp.base.BaseViewHolder
import com.wasim.csdl.patient_chatapp.databinding.UserSearchItemLayoutBinding
import com.wasim.csdl.patient_chatapp.models.Chat
import com.wasim.csdl.patient_chatapp.models.Users
import com.wasim.csdl.patient_chatapp.utils.USER_ID
import com.wasim.csdl.patient_chatapp.utils.singleClick
import com.wasim.csdl.patient_chatapp.views.activities.MessageChatActivity
import com.wasim.csdl.patient_chatapp.views.activities.VisitUserProfileActivity

class UserAdapter(var context: Context, isChatCheck: Boolean) :
    BaseAdapter<UserAdapter.UserAdapterViewHolder, Users>() {

    private var isChatCheck: Boolean
    private var lastMsg: String = ""

    init {
        this.isChatCheck = isChatCheck
    }

    class UserAdapterViewHolder(itemView: View) : BaseViewHolder(itemView) {}

    override val layoutId: Int get() = R.layout.user_search_item_layout


    override fun setData(holder: UserAdapterViewHolder, model: Users, position: Int) {
        val view = holder.itemView
        val binding = UserSearchItemLayoutBinding.bind(view)

        // val user: Users = mUser[position]
        binding.userName.text = model.user_Name
        Picasso.get().load(model.getProfile()).placeholder(R.drawable.profile)
            .into(binding.userProfileImage)

        if (isChatCheck) {
            retriveLastMessage(model.getUSerId(), binding.messageLast)
        } else {
            binding.messageLast.visibility = View.GONE
        }

        if (isChatCheck) {
            if (model.getStatus() == "online") {
                binding.imageOnline.visibility = View.VISIBLE
                binding.imageOffline.visibility = View.GONE
            } else {
                binding.imageOnline.visibility = View.GONE
                binding.imageOffline.visibility = View.VISIBLE

            }
        } else {
            binding.imageOnline.visibility = View.GONE
            binding.imageOffline.visibility = View.GONE
        }

        holder.itemView.singleClick {
            val option = arrayOf<CharSequence>(
                "Send Message",
                "Visit Profile"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("What do you want?")

            builder.setItems(option, DialogInterface.OnClickListener { dialog, position ->
                if (position == 0) {
                    val intent = Intent(context, MessageChatActivity::class.java)
                    intent.putExtra(USER_ID, model.getUSerId())
                    Log.i("UserAdapter", "User id Adapter  : ${model.getUSerId()}")
                    context.startActivity(intent)
                }
                if (position == 1) {

                    val intent = Intent(context, VisitUserProfileActivity::class.java)
                    intent.putExtra(USER_ID, model.getUSerId())
                    Log.i("UserAdapter", "User id Adapter  : ${model.getUSerId()}")
                    context.startActivity(intent)
                }
            })

            builder.show()
        }

    }

    private fun retriveLastMessage(onLineUserId: String, messageLast: TextView) {

        lastMsg = "defaultMsg"

        val firebaseUSer = FirebaseAuth.getInstance().currentUser
        val refrence = FirebaseDatabase.getInstance().reference.child("Chats")

        refrence.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {

                for (dataSnapshot in p0.children) {
                    val chat: Chat? = dataSnapshot.getValue(Chat::class.java)
                    if (firebaseUSer != null && chat != null) {
                        if (chat!!.getReceiver().equals(firebaseUSer!!.uid) && chat!!.getSender()
                                .equals(onLineUserId) ||
                            chat!!.getReceiver().equals(onLineUserId) && chat!!.getSender()
                                .equals(firebaseUSer!!.uid)
                        ) {
                            lastMsg = chat.getMessage()!!
                        }
                    }
                }

                when (lastMsg) {
                    "defaultMsg" -> messageLast.text = "No Message"
                    "sent you an image." -> messageLast.text = "Image Sent"
                    else -> messageLast.text = lastMsg
                }

                lastMsg = "defaultMsg"
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}

