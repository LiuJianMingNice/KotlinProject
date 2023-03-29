package com.example.myapplication.guolin

import android.content.Context
import androidx.startup.Initializer

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * LitePalInitializer
 * @author liujianming
 * @date 2023-03-14
 */
class LitePalInitializer : Initializer<Unit> {

    override fun create(context: Context) {
//        LitePal.initialize(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        TODO("Not yet implemented")
    }
}