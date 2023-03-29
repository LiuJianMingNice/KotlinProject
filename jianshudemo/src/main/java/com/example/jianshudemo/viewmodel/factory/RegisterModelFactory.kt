package com.example.jianshudemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jianshudemo.dp.repository.UserRepository
import com.example.jianshudemo.viewmodel.RegisterModel

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * RegisterModelFactory
 * @author liujianming
 * @date 2023-02-13
 */
class RegisterModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterModel(repository) as T
    }
}