package com.example.jianshudemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jianshudemo.viewmodel.FavouriteModel
import com.joe.jetpackdemo.db.repository.ShoeRepository

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * FavouriteModelFactory
 * @author liujianming
 * @date 2023-02-13
 */
class FavouriteModelFactory(private val repository: ShoeRepository
,private val userId: Long) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouriteModel(repository,userId) as T
    }
}