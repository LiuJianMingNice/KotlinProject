package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.bean.User

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * MainViewModel
 * @author liujianming
 * @date 2021-12-22
 */
class MainViewModel: ViewModel() {
    private val _user:MutableLiveData<User> = MutableLiveData(User(1, "测试"))
    val mUser: LiveData<User> = _user
}