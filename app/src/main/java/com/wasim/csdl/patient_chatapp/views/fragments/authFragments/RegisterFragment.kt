package com.wasim.csdl.patient_chatapp.views.fragments.authFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseFragments
import com.wasim.csdl.patient_chatapp.databinding.FragmentRegisterBinding
import com.wasim.csdl.patient_chatapp.utils.isNetworkAvailable
import com.wasim.csdl.patient_chatapp.utils.singleClick
import com.wasim.csdl.patient_chatapp.viewModels.BaseViewModel
import com.wasim.csdl.patient_chatapp.views.activities.MainActivity
import java.util.Locale


class RegisterFragment : BaseFragments<BaseViewModel>() {
    override val viewModelClass: Class<BaseViewModel>
        get() = BaseViewModel::class.java

    lateinit var binding: FragmentRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUser: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun observeLiveData() {
    }

    override fun init() {
        customToobarACtion()
        mAuth = FirebaseAuth.getInstance()
        binding.btnRegsiter.singleClick {

            registerUser()
        }
    }

    private fun registerUser() {
        val userName = binding.userNameRegister.text.trim().toString()
        val email = binding.emailRegister.text.trim().toString()
        val password = binding.passwordRegister.text.toString()

        if(isNetworkAvailable(currentActivity())){

        if (userName == "") {
            showToast("Please enter username")
            binding.userNameRegister.requestFocus()
        } else if (email == "") {
            showToast("Please enter email")
            binding.emailRegister.requestFocus()
        } else if (password == "") {
            showToast("Please enter password")
            binding.passwordRegister.requestFocus()
        } else {
            showLoadingBar()
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUser = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(firebaseUserID)
                        val userHashMap = HashMap<String, Any>()
                        userHashMap["user_ID"] = firebaseUserID
                        userHashMap["user_Name"] = userName
                        userHashMap["profile"] =
                            "https://firebasestorage.googleapis.com/v0/b/patientchatapp-26fc4.appspot.com/o/profile.png?alt=media&token=e5c83099-d16b-48f8-9d57-b2e3c8eea620"
                        userHashMap["cover"] =
                            "https://firebasestorage.googleapis.com/v0/b/patientchatapp-26fc4.appspot.com/o/cover_pic.jpg?alt=media&token=dbf13358-ac87-45bd-b9fe-972c982ee789"
                        userHashMap["status"] = "offline"
                        userHashMap["search"] = userName.lowercase(Locale.getDefault())
                        userHashMap["facebook"] = "https://m.facebook.com"
                        userHashMap["instagram"] = "https://m.instagram.com"
                        userHashMap["website"] = "https://www.google.com"

                        refUser.updateChildren(userHashMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    hideLoadingBar()
                                    showToast("Register Successfully")
                                    startActivity(Intent(currentActivity(), MainActivity::class.java))
                                    currentActivity().finish()
                                }
                            }

                    } else {
                        showToast("Error: " + task.exception?.message)
                        hideLoadingBar()
                    }
                }
        }
        }else{
            showToast("Please check your internet connection")
        }
    }

    private fun customToobarACtion() {
        val myToolbar =
            currentActivity().findViewById(R.id.toolbarRgister) as androidx.appcompat.widget.Toolbar
        myToolbar.title = "Register"
        currentActivity().setSupportActionBar(myToolbar)

        val actionBar = currentActivity().supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        myToolbar.setNavigationOnClickListener {
            currentActivity().onBackPressed()
        }
    }

}