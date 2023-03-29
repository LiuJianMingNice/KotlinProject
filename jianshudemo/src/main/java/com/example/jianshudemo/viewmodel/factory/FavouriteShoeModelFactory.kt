package com.example.jianshudemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jianshudemo.viewmodel.DetailModel
import com.joe.jetpackdemo.db.repository.FavouriteShoeRepository
import com.joe.jetpackdemo.db.repository.ShoeRepository

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * FavouriteShoeModelFactory
 * @author liujianming
 * @date 2023-02-15
 */
class FavouriteShoeModelFactory(
    private val shoeRepository: ShoeRepository
    , private val favouriteShoeRepository: FavouriteShoeRepository
    , private val shoeId: Long
    , private val userId: Long
    ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailModel(shoeRepository, favouriteShoeRepository, shoeId, userId) as T
    }
}