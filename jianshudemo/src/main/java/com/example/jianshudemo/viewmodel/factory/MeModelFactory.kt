package com.example.jianshudemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jianshudemo.dp.repository.UserRepository
import com.example.jianshudemo.viewmodel.MeModel

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * MeModelFactory
 * @author liujianming
 * @date 2023-02-13
 */
class MeModelFactory(private val repository: UserRepository) :ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MeModel(repository) as T
    }
}