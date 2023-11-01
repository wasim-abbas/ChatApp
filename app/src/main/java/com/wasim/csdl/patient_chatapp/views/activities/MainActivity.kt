package com.wasim.csdl.patient_chatapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseActivity
import com.wasim.csdl.patient_chatapp.databinding.ActivityMainBinding
import com.wasim.csdl.patient_chatapp.models.Chat
import com.wasim.csdl.patient_chatapp.models.Users
import com.wasim.csdl.patient_chatapp.views.fragments.mainFragments.ChatFragment
import com.wasim.csdl.patient_chatapp.views.fragments.mainFragments.SearchFragment
import com.wasim.csdl.patient_chatapp.views.fragments.mainFragments.SettingFragment

class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var refUser: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)
        supportActionBar!!.title = ""

//        val viewPageradapter = ViewPagerAdapter(supportFragmentManager)
//        viewPageradapter.addFragment(ChatFragment(), "Chats")
//        viewPageradapter.addFragment(SearchFragment(), "Search")
//        viewPageradapter.addFragment(SettingFragment(), "Setting")
//        binding.viewPager.adapter = viewPageradapter
//        binding.tabLayout.setupWithViewPager(binding.viewPager)

        seenChatCounter()
        displayUser()

    }

    private fun seenChatCounter() {

        val ref = FirebaseDatabase.getInstance().reference.child("Chats")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val viewPageradapter = ViewPagerAdapter(supportFragmentManager)
                var countUnreadMessages = 0
                for (dataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)

                    if (chat!!.getReceiver().equals(firebaseUser!!.uid) && !chat.getIsSeen()!!) {
                        countUnreadMessages += 1
                    }
                }
                if (countUnreadMessages == 0) {
                    viewPageradapter.addFragment(ChatFragment(), "Chats")
                } else {
                    viewPageradapter.addFragment(ChatFragment(), "($countUnreadMessages) Chats")
                }

                viewPageradapter.addFragment(SearchFragment(), "Search")
                viewPageradapter.addFragment(SettingFragment(), "Setting")

                binding.viewPager.adapter = viewPageradapter
                binding.tabLayout.setupWithViewPager(binding.viewPager)
            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }

    private fun displayUser() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUser = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        refUser!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: Users? = snapshot.getValue(Users::class.java)
                    binding.userName.text = user!!.user_Name
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile)
                        .into(binding.prfileImg)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun attachViewMode() {
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_Logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, AuthActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.finish()
                return true
            }
        }
        return false
    }

    private fun updateStatus(status: String) {
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        val hashMap = HashMap<String, Any>()
        hashMap["status"] = status
        ref.updateChildren(hashMap)
    }

    override fun onResume() {
        super.onResume()
        updateStatus("online")
    }

    override fun onPause() {
        super.onPause()
        updateStatus("offline")
    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {
        private val fragments = ArrayList<Fragment>()
        private val titles = ArrayList<String>()

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}