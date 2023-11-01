package com.wasim.csdl.patient_chatapp.notifications

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.FirebaseMessaging

class MyFirebaseInstanceId : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        // val refreshToken : String= FirebaseMessaging.getInstance().token.toString()


        //  Log.d("Token",refreshToken)


        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result

                Log.d("Refreshed token1", token)
                if (firebaseUser != null) {
                    updateToken(token)
                }

            }
        }

//        if (firebaseUser != null)
//        {
//            updateToken(refreshToken!!)
//        }

    }


    private fun updateToken(refreshToken: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().getReference().child("Tokens")
        val token = Token(refreshToken)
        ref.child(firebaseUser!!.uid).setValue(token)
    }
}