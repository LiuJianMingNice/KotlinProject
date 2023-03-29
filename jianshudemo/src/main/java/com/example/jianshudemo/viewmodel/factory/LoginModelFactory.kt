package com.example.jianshudemo.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jianshudemo.dp.repository.UserRepository
import com.example.jianshudemo.viewmodel.LoginModel

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * LoginModelFactory
 * @author liujianming
 * @date 2023-02-13
 */
class LoginModelFactory(private val repository: UserRepository
, private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginModel(repository) as T
    }
}