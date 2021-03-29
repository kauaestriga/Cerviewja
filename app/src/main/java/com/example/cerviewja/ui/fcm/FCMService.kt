package com.example.cerviewja.ui.fcm

import android.content.Intent
import com.example.cerviewja.R
import com.example.cerviewja.ui.main.MainActivity
import com.example.cerviewja.utils.notification.NotificationUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        val title = p0.data["title"] ?: p0.notification?.title ?:
        this.getString(R.string.app_name)
        val message = p0.data["message"] ?: p0.notification?.body ?: ""
        val channelID = this.getString(R.string.default_notification_channel_id)
        val channelName = this.getString(R.string.default_notification_channel_name)
        val channelDescription =
            this.getString(R.string.default_notification_channel_description)
        val notifChannel =
            NotificationUtils.NotifChannel(channelID, channelName, channelDescription)
        NotificationUtils.notificationSimple(this)
    }
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}