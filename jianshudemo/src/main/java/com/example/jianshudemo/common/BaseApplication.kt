package com.example.jianshudemo.common

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * BaseApplication
 * @author liujianming
 * @date 2023-02-07
 */
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        // MMKV 初始化
        val root = context.filesDir.absolutePath + "/mmkv"
        MMKV.initialize(root, MMKVLogLevel.LevelInfo)
    }

    companion object {
        lateinit var context: Context
    }
}