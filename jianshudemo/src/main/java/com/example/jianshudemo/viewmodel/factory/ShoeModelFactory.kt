package com.example.jianshudemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jianshudemo.viewmodel.ShoeModel
import com.joe.jetpackdemo.db.repository.ShoeRepository

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * ShoeModelFactory
 * @author liujianming
 * @date 2023-02-14
 */
class ShoeModelFactory(private val repository: ShoeRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoeModel(repository) as T
    }
}