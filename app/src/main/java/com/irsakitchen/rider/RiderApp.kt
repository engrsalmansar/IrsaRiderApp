package com.irsakitchen.rider

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging

class RiderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }
}
