package com.wasim.csdl.patient_chatapp.views.fragments.mainFragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wasim.csdl.patient_chatapp.base.BaseFragments
import com.wasim.csdl.patient_chatapp.databinding.FragmentSearchBinding
import com.wasim.csdl.patient_chatapp.models.Users
import com.wasim.csdl.patient_chatapp.viewModels.BaseViewModel
import com.wasim.csdl.patient_chatapp.views.adapters.UserAdapter
import java.util.Locale


class SearchFragment : BaseFragments<BaseViewModel>() {
    override val viewModelClass: Class<BaseViewModel>
        get() = BaseViewModel::class.java
    lateinit var binding: FragmentSearchBinding

    private lateinit var userAdapter: UserAdapter
    private var mUser: ArrayList<Users>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun observeLiveData() {
// to do
    }

    override fun init() {

        binding.RVSearchedUser.hasFixedSize()
        mUser = ArrayList()
        retriveAllUsers()

        binding.edSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchUser(char.toString().lowercase(Locale.getDefault()))
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    private fun retriveAllUsers() {
        var firebaseUserID =
            FirebaseAuth.getInstance().currentUser!!.uid  /// get the refrece id current user
        val refUsers = FirebaseDatabase.getInstance().reference.child("Users")

        try {


        refUsers.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                (mUser as ArrayList<Users>).clear()
                if (binding.edSearch.text.toString() == "") {
                    for (p0 in snapshot.children) {
                        val user: Users? = p0.getValue(Users::class.java)
                        if (!(user!!.getUSerId()).equals(firebaseUserID)) {
                            (mUser as ArrayList<Users>).add(user)
                        }
                    }
                }

                userAdapter = UserAdapter(context!!, false)
                userAdapter.updateData(mUser!!)
                binding.RVSearchedUser.adapter = userAdapter


            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun searchUser(str: String) {

        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val queryUser =
            FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search")
                .startAt(str)
                .endAt(str + "\uf8ff")

        queryUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUser as ArrayList<Users>).clear()
                for (p0 in snapshot.children) {
                    val user: Users? = p0.getValue(Users::class.java)
                    if (!(user!!.getUSerId()).equals(firebaseUserID)) {
                        (mUser as ArrayList<Users>).add(user)
                    }
                }

                userAdapter = UserAdapter(context!!, false)
                userAdapter.updateData(mUser!!)
                binding.RVSearchedUser.adapter = userAdapter

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}