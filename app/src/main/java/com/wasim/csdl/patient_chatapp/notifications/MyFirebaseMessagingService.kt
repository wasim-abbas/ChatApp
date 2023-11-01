package com.wasim.csdl.patient_chatapp.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wasim.csdl.patient_chatapp.utils.USER_ID
import com.wasim.csdl.patient_chatapp.views.activities.MessageChatActivity
import java.lang.NumberFormatException
import kotlin.math.log

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(mRemotemessage: RemoteMessage) {
        super.onMessageReceived(mRemotemessage)

        val sented = mRemotemessage.data["sented"]

        val user = mRemotemessage.data["user"]

        val sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE)

        val currentOnlineUser = sharedPref.getString("currentUser", "none")

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null && sented == firebaseUser.uid) {

            if (currentOnlineUser != user) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    sendOreoNotification(mRemotemessage)
                } else {
                    sendNotification(mRemotemessage)
                }
            }
        }
    }

    private fun sendNotification(mRemotemessage: RemoteMessage) {

        val user = mRemotemessage.data["user"]
        val icon = mRemotemessage.data["icon"]
        val title = mRemotemessage.data["title"]
        val body = mRemotemessage.data["body"]

        val notification = mRemotemessage.notification

        val j = user!!.replace("[\\D]".toRegex(), "").toLong()

        val intent = Intent(this, MessageChatActivity::class.java)
        val bundle = Bundle()
        bundle.putString(USER_ID, user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingingIntent = PendingIntent.getActivity(
            this, j.toInt(), intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(icon!!.toInt())
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingingIntent)

        val noti = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var i = 0

        if (j > 0) {
            i = j.toInt()
        }

        noti.notify(i, builder.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendOreoNotification(mRemotemessage: RemoteMessage) {

        val user = mRemotemessage.data["user"]
        val icon = mRemotemessage.data["icon"]
        val title = mRemotemessage.data["title"]
        val body = mRemotemessage.data["body"]

        try {


        val notification = mRemotemessage.notification

        val j = user!!.replace("[\\D]".toRegex(), "").toLong()

        val intent = Intent(this, MessageChatActivity::class.java)
        val bundle = Bundle()
        bundle.putString(USER_ID, user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingingIntent = PendingIntent.getActivity(
            this, j.toInt(), intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val oreoNotification = OreoNotification(this)

        val builder: Notification.Builder =
            oreoNotification.getOreoNotification(title, body, pendingingIntent, defaultSound, icon)

        var i = 0

        if (j > 0) {
            i = j.toInt()
        }

        oreoNotification.getManager!!.notify(i, builder.build())
        }catch (e: NumberFormatException){
            e.printStackTrace()
        }
    }
}