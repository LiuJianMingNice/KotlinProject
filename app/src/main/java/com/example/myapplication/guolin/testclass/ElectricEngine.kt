package com.example.myapplication.guolin.testclass

import android.util.Log
import com.example.myapplication.guolin.interfaces.Engine
import javax.inject.Inject

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * ElectricEngine
 * @author liujianming
 * @date 2023-03-15
 */
class ElectricEngine @Inject constructor(): Engine {
    override fun start() {
        Log.d("ljm", "Electric engine start.")
    }

    override fun shutdown() {
        Log.d("ljm", "Electric engine shutdown.")
    }
}