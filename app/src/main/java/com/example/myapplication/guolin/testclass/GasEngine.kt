package com.example.myapplication.guolin.testclass

import android.util.Log
import com.example.myapplication.guolin.interfaces.Engine
import javax.inject.Inject

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * GasEngine
 * @author liujianming
 * @date 2023-03-15
 */
class GasEngine @Inject constructor(): Engine {
    override fun start() {
        Log.d("ljm", "Gas engine start.")
    }

    override fun shutdown() {
        Log.d("ljm", "Gas engine shutdown.")
    }
}