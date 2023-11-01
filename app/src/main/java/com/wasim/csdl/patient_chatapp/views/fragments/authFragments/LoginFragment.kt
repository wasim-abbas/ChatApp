package com.wasim.csdl.patient_chatapp.views.fragments.authFragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseFragments
import com.wasim.csdl.patient_chatapp.databinding.FragmentLoginBinding
import com.wasim.csdl.patient_chatapp.utils.isNetworkAvailable
import com.wasim.csdl.patient_chatapp.utils.singleClick
import com.wasim.csdl.patient_chatapp.viewModels.BaseViewModel
import com.wasim.csdl.patient_chatapp.views.activities.MainActivity


class LoginFragment : BaseFragments<BaseViewModel>() {
    override val viewModelClass: Class<BaseViewModel>
        get() = BaseViewModel::class.java

    lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun observeLiveData() {
   //to do
    }

    override fun init() {
        customToobarACtion()

        mAuth = FirebaseAuth.getInstance()
        binding.btnLogin.singleClick {

            loginUser()
        }

        binding.btnCreateAccount.singleClick {
            currentActivity().replaceFragmentInAuth(R.id.action_loginFragment_to_registerFragment)
        }
    }



    private fun loginUser() {


        val email = binding.emailLogin.text.trim().toString()
        val password = binding.passwordLogin.text.trim().toString()

        if(isNetworkAvailable(currentActivity())){


        if (email == "") {
            showToast("Please enter email")
            binding.emailLogin.requestFocus()
        } else if (password == "") {
            showToast("Please enter password")
            binding.passwordLogin.requestFocus()
        } else {
            showLoadingBar()
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hideLoadingBar()
                        val intent = Intent(currentActivity(), MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        currentActivity().finish()
                    } else {
                        hideLoadingBar()
                        showToast(task.exception!!.message.toString())
                        showToast("Wrong email or password")
                    }
                }
        }
        }else{
            showToast("Please check your internet connection")
        }
    }

    private fun customToobarACtion() {
        val myToolbar =
            currentActivity().findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        myToolbar.title = "Login"
    }
}