package com.wasim.csdl.patient_chatapp.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseActivity
import com.wasim.csdl.patient_chatapp.databinding.ActivityVisitUserProfileBinding
import com.wasim.csdl.patient_chatapp.models.Users
import com.wasim.csdl.patient_chatapp.utils.USER_ID
import com.wasim.csdl.patient_chatapp.utils.singleClick

class VisitUserProfileActivity : BaseActivity() {

    val binding by lazy {
        ActivityVisitUserProfileBinding.inflate(layoutInflater)
    }

    var userIdVisit: String? = ""
    var user: Users? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userIdVisit = intent.getStringExtra(USER_ID)

        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(userIdVisit!!)
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(Users::class.java)

                    binding.tvUSerName.text = user!!.user_Name
                    Picasso.get().load(user!!.getProfile()).placeholder(R.drawable.profile)
                        .into(binding.imgProfileSetting)

                    Picasso.get().load(user!!.getCover()).placeholder(R.drawable.cover_pic)
                        .into(binding.imgCover)

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.btnSendMEssage.singleClick {
            val intent = Intent(this, MessageChatActivity::class.java)
            intent.putExtra(USER_ID, user!!.getUSerId())
            Log.i("UserAdapter", "User id Adapter  : ${user!!.getUSerId()}")
            startActivity(intent)
        }
    }

    override fun attachViewMode() {}
}