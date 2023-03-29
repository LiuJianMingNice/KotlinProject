package com.example.myapplication.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * RegisterViewModel
 * @author liujianming
 * @date 2021-12-29
 */
class RegisterViewModel {

    var isSaveEnable = MutableLiveData<Boolean>(false)
//    var isSaveEnable : Boolean = true

    init {
        isSaveEnable.value = false
    }

    fun saveDeviceInfo() {

    }
}