package com.wasim.csdl.patient_chatapp.base

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.wasim.csdl.patient_chatapp.R
import io.github.rupinderjeet.kprogresshud.KProgressHUD

abstract class BaseActivity : AppCompatActivity() {

    var progressBar: KProgressHUD? = null
    private lateinit var options: NavOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressBar = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(true)
            .setBackgroundColor(Color.TRANSPARENT)
            .setAnimationSpeed(1)
            .setDimAmount(0.5f)

        options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()


    }

    fun showLoadingBar() {
        progressBar?.show()
    }

    fun hideLoadingBar() {
        progressBar?.dismiss()
    }

    abstract fun attachViewMode()

    fun replaceMainFragment(action: Int) {
        try {
            Navigation.findNavController(this, R.id.authHostFragment)
                .navigate(action, null, options)
        } catch (e: java.lang.IllegalArgumentException) {
            e.printStackTrace()
        }

    }

    fun replaceMainFragment(action: Int, bundle: Bundle) {

        try {

            Navigation.findNavController(this, R.id.authHostFragment)
                .navigate(action, bundle, options)
        } catch (e: java.lang.IllegalArgumentException) {
            e.printStackTrace()
        }


    }

    fun replaceAndRemoveMainFragment(action1: Int, action2: Int, bundle: Bundle? = null) {
        Navigation.findNavController(this, R.id.authHostFragment).popBackStack(action1, true)
        Navigation.findNavController(this, R.id.authHostFragment)
            .navigate(action2, bundle, options)

    }
//
    fun replaceFragmentInAuth(action: Int) {

        try {
            Navigation.findNavController(this, R.id.authHostFragment)
                .navigate(action, null, options)
        } catch (e: IllegalArgumentException) {
            // Handle exception here
            e.printStackTrace()
        }

    }
//
//    fun replaceFragmentInAuth(action: Int, bundle: Bundle) {
//        try {
//            Navigation.findNavController(this, R.id.authHostFragment)
//                .navigate(action, bundle, options)
//        } catch (e: IllegalArgumentException) {
//            // Handle exception here
//            e.printStackTrace()
//        }
//
//    }
//
//    fun replaceAndRemoveFragmentInAuth(action1: Int, action2: Int, bundle: Bundle? = null) {
//        Navigation.findNavController(this, R.id.authHostFragment).popBackStack(action1, true)
//        Navigation.findNavController(this, R.id.authHostFragment).navigate(action2, bundle, options)
//
//    }
//    }


    fun showToast(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT)
                .show()
        }

    }


}