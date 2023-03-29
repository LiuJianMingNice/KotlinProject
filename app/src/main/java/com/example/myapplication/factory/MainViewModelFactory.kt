package com.example.myapplication.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.MainRepository
import com.example.myapplication.viewmodel.MainViewModel

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * MainViewModelFactory
 * @author liujianming
 * @date 2021-12-22
 */
class MainViewModelFactory(private val repository: MainRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}