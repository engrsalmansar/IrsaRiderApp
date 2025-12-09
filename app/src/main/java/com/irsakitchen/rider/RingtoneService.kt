package com.irsakitchen.rider

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class RingtoneService : Service() {

    private var ring: Ringtone? = null

    override fun onStartCommand(intent: Intent?, flags: Int, id: Int): Int {
        val orderId = intent?.getStringExtra("order_id") ?: "0"

        createChannel()

        val noti = NotificationCompat.Builder(this, "ringing")
            .setContentTitle("Incoming Order #$orderId")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        startForeground(1, noti)

        val alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ring = RingtoneManager.getRingtone(this, alarm)
        ring?.isLooping = true
        ring?.play()

        return START_STICKY
    }

    override fun onDestroy() {
        ring?.stop()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(
                NotificationChannel("ringing", "Ringing", NotificationManager.IMPORTANCE_HIGH)
            )
        }
    }
}
