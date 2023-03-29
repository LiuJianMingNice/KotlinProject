package com.amazing.eye

import android.app.Application
import com.example.myapplication.mvvm.ApplicationContext

class EyeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationContext.initContext(this)
    }
}