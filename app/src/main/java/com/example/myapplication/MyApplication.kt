package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * MyApplication
 * @author liujianming
 * @date 2021-12-22
 */
@HiltAndroidApp
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}