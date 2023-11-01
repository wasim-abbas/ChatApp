package com.wasim.csdl.patient_chatapp.views.activities

import android.os.Bundle
import com.wasim.csdl.patient_chatapp.base.BaseActivity
import com.wasim.csdl.patient_chatapp.databinding.ActivityAuthBinding

class AuthActivity : BaseActivity() {

    private val binding: ActivityAuthBinding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun attachViewMode() {
    }
}