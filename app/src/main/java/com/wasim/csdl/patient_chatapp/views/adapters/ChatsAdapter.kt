package com.wasim.csdl.patient_chatapp.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.wasim.csdl.patient_chatapp.models.Chat
import com.wasim.csdl.patient_chatapp.utils.singleClick
import com.wasim.csdl.patient_chatapp.views.activities.ViewFullImageActivity
import de.hdodenhof.circleimageview.CircleImageView

class ChatsAdapter(var context: Context, mChatlist: List<Chat>, imageUrl: String?) :
    RecyclerView.Adapter<ChatsAdapter.ViewHolder?>() {


    private val mContext: Context?
    private val mchatList: List<Chat>?
    private val imageUrl: String?
    var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    init {
        this.mchatList = mChatlist
        this.mContext = context
        this.imageUrl = imageUrl
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {

        return if (position == 1) {
            val view: View = LayoutInflater.from(mContext)
                .inflate(com.wasim.csdl.patient_chatapp.R.layout.mesage_item_right, parent, false)
            ViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(mContext)
                .inflate(com.wasim.csdl.patient_chatapp.R.layout.message_item_left, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mchatList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val chat: Chat = mchatList!![position]
        Picasso.get().load(imageUrl).into(holder.profile_image)

        ///Images Messages
        if (chat.getMessage().equals("sent you an image.") && !chat.getUrl().equals("")) {

            //image message  -> Right side
            if (chat.getSender().equals(firebaseUser!!.uid)) {

                holder.show_text_message!!.visibility = View.GONE
                holder.right_image_view!!.visibility = View.VISIBLE
                Picasso.get().load(chat.getUrl()).into(holder.right_image_view)

                holder.right_image_view!!.singleClick {

                    val options = arrayOf<CharSequence>(
                        "View Full Image",
                        "Delete Image",
                        "Cancel"
                    )

                    var builder: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want?")

                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            //View Full Image
                            val intent = Intent(mContext, ViewFullImageActivity::class.java)
                            intent.putExtra("url", chat.getUrl())
                            mContext?.startActivity(intent)
                        } else if (which == 1) {
                            //Delete Image
                            deleteSentMessage(position, holder)
                        }
                    }
                    builder.show()
                }
            }
            //image message  -> Let side
            else if (!chat.getSender().equals(firebaseUser!!.uid)) {

                holder.show_text_message!!.visibility = View.GONE
                holder.left_image_view!!.visibility = View.VISIBLE
                Picasso.get().load(chat.getUrl()).into(holder.left_image_view)

                holder.left_image_view!!.singleClick {

                    val options = arrayOf<CharSequence>(
                        "View Full Image",
                        "Cancel"
                    )

                    var builder: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want?")

                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            //View Full Image
                            val intent = Intent(mContext, ViewFullImageActivity::class.java)
                            intent.putExtra("url", chat.getUrl())
                            mContext?.startActivity(intent)
                        }
                    }
                    builder.show()
                }
            }
        } else {
            ///Text messages
            holder.show_text_message!!.text = chat.getMessage()

          if(firebaseUser!!.uid == chat.getSender()){
              holder.show_text_message!!.singleClick {
                  val options = arrayOf<CharSequence>(
                      "Delete Message",
                      "Cancel"
                  )

                  var builder: android.app.AlertDialog.Builder =
                      android.app.AlertDialog.Builder(holder.itemView.context)
                  builder.setTitle("What do you want?")

                  builder.setItems(options) { dialog, which ->
                      if (which == 0) {
                          //Delete Message
                          deleteSentMessage(position, holder)
                      }
                  }
                  builder.show()
              }
          }

        }


        // Sent and seen messages
        if (position == mchatList.size - 1) {
            if (chat.getIsSeen()!!) {

                holder.text_message_seen!!.text = "seen"

                if (chat.getMessage().equals("sent you an image.") && !chat.getUrl().equals("")) {

                    val lp: RelativeLayout.LayoutParams? =
                        holder.text_message_seen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0, 245, 10, 0)
                    holder.text_message_seen!!.layoutParams = lp
                }

            } else {

                holder.text_message_seen!!.text = "sent"

                if (chat.getMessage().equals("sent you an image.") && !chat.getUrl().equals("")) {

                    val lp: RelativeLayout.LayoutParams? =
                        holder.text_message_seen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0, 245, 10, 0)
                    holder.text_message_seen!!.layoutParams = lp
                }
            }
        } else {
            holder.text_message_seen!!.visibility = View.GONE
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (mchatList!![position].getSender().equals(firebaseUser!!.uid)) {
            1
        } else {
            0
        }
    }

    private fun deleteSentMessage(position: Int, holder: ChatsAdapter.ViewHolder) {

        val refe = FirebaseDatabase.getInstance().reference.child("Chats")
            .child(mchatList!![position].getMessageID()!!).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(holder.itemView.context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var profile_image: CircleImageView? = null
        var show_text_message: TextView? = null
        var left_image_view: ImageView? = null
        var right_image_view: ImageView? = null
        var text_message_seen: TextView? = null

        init {
            profile_image = itemView.findViewById(com.wasim.csdl.patient_chatapp.R.id.profile_image)
            show_text_message =
                itemView.findViewById(com.wasim.csdl.patient_chatapp.R.id.show_text_message)
            left_image_view =
                itemView.findViewById(com.wasim.csdl.patient_chatapp.R.id.left_image_view)
            right_image_view =
                itemView.findViewById(com.wasim.csdl.patient_chatapp.R.id.right_image_view)
            text_message_seen =
                itemView.findViewById(com.wasim.csdl.patient_chatapp.R.id.text_message_seen)
        }
    }
}