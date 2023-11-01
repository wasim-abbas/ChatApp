package com.wasim.csdl.patient_chatapp.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseActivity
import com.wasim.csdl.patient_chatapp.databinding.ActivityViewFullImageBinding

class ViewFullImageActivity : BaseActivity() {

    private val binding: ActivityViewFullImageBinding by lazy {
        ActivityViewFullImageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra("url")

        Picasso.get().load(imageUrl).into(binding.fullImageView)

    }

    override fun attachViewMode() {
    }
}