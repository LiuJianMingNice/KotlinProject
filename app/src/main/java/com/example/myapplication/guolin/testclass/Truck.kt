package com.example.myapplication.guolin.testclass

import android.util.Log
import com.example.myapplication.guolin.annotations.BindElectricEngine
import com.example.myapplication.guolin.annotations.BindGasEngine
import com.example.myapplication.guolin.interfaces.Engine
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * Truck
 * @author liujianming
 * @date 2023-03-15
 */
class Truck @Inject constructor(val driver: Driver) {

    @BindGasEngine
    @Inject
    lateinit var gasEngine: Engine

    @BindElectricEngine
    @Inject
    lateinit var electricEngine: Engine

    @Inject
    lateinit var okHttpClient: OkHttpClient

    fun deliver() {
        gasEngine.start()
        electricEngine.start()
        Log.d("ljm", "Truck is delivering cargo.Driver by $driver")
        gasEngine.shutdown()
        electricEngine.shutdown()
    }
}