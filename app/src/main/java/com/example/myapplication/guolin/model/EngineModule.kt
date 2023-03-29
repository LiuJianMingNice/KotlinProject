package com.example.myapplication.guolin.model

import com.example.myapplication.guolin.annotations.BindElectricEngine
import com.example.myapplication.guolin.annotations.BindGasEngine
import com.example.myapplication.guolin.interfaces.Engine
import com.example.myapplication.guolin.testclass.ElectricEngine
import com.example.myapplication.guolin.testclass.GasEngine
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * EngineModule
 * @author liujianming
 * @date 2023-03-15
 */

@Module
@InstallIn(ActivityComponent::class)
abstract class EngineModule {

    @BindGasEngine
    @Binds
    abstract fun bindGasEngine(gasEngine: GasEngine): Engine

    @BindElectricEngine
    @Binds
    abstract fun bindElectricEngine(electricEngine : ElectricEngine): Engine
}