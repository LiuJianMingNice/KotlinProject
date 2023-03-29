package com.example.myapplication.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * PlayBackTypeViewModel
 * @author liujianming
 * @date 2022-03-23
 */
class PlayBackTypeViewModel: ViewModel() {

    var isShowImage = MutableLiveData<Boolean>(false)
    var playbackTypeName = MutableLiveData<String>("")
    var isSelect = MutableLiveData<Boolean>(false)

    fun onCheckBoxClick(view: View) {

    }
}