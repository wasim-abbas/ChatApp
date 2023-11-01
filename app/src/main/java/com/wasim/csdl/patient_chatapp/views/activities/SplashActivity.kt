package com.wasim.csdl.patient_chatapp.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseActivity
import com.wasim.csdl.patient_chatapp.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun attachViewMode() {
    }
}