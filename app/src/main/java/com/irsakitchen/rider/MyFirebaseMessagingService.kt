package com.irsakitchen.rider

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(msg: RemoteMessage) {
        val data = msg.data
        if (data["type"] == "new_order") {
            val orderId = data["order_id"] ?: "0"
            val subtitle = data["subtitle"] ?: "New delivery"

            showNotification(orderId, subtitle)

            // start full-screen ringing
            val i = Intent(this, IncomingOrderActivity::class.java)
            i.putExtra("order_id", orderId)
            i.putExtra("subtitle", subtitle)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)

            val s = Intent(this, RingtoneService::class.java)
            s.putExtra("order_id", orderId)
            startForegroundService(s)
        }
    }

    private fun showNotification(orderId: String, subtitle: String) {
        val channelId = "irsa_orders"

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(
            NotificationChannel(channelId, "Orders", NotificationManager.IMPORTANCE_HIGH)
        )

        val intent = Intent(this, IncomingOrderActivity::class.java)
        intent.putExtra("order_id", orderId)
        val pi = PendingIntent.getActivity(this, orderId.hashCode(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val noti = NotificationCompat.Builder(this, channelId)
            .setContentTitle("New Order #$orderId")
            .setContentText(subtitle)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        nm.notify(orderId.hashCode(), noti)
    }
}
