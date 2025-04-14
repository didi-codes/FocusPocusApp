package com.productivitybandits.focuspocusapp

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApps : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}