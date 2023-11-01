package com.wasim.csdl.patient_chatapp.views.fragments.authFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseFragments
import com.wasim.csdl.patient_chatapp.databinding.FragmentSplashBinding
import com.wasim.csdl.patient_chatapp.viewModels.BaseViewModel
import com.wasim.csdl.patient_chatapp.views.activities.AuthActivity
import com.wasim.csdl.patient_chatapp.views.activities.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : BaseFragments<BaseViewModel>() {
    override val viewModelClass: Class<BaseViewModel>
        get() = BaseViewModel::class.java
    lateinit var binding: FragmentSplashBinding

    var firbaseUSer: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun observeLiveData() {
    }

    override fun init() {

        firbaseUSer = FirebaseAuth.getInstance().currentUser
        MainScope().launch {
            delay(2000)
            if(firbaseUSer != null){
                Log.i("saved token", firbaseUSer!!.uid)
                startActivity(Intent(currentActivity(), MainActivity::class.java))
                currentActivity().finish()
            }
            else{
                startActivity(Intent(currentActivity(), AuthActivity::class.java))
                currentActivity().finish()
            }
        }
    }

}