package com.example.jianshudemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jianshudemo.viewmodel.StorageModel
import com.joe.jetpackdemo.db.repository.StorageDataRepository

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * StorageModelFactory
 * @author liujianming
 * @date 2023-02-14
 */
class StorageModelFactory(private val repository: StorageDataRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StorageModel(repository) as T
    }
}