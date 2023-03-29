package com.example.jianshudemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jianshudemo.dp.data.FavouriteShoe
import com.example.jianshudemo.dp.data.Shoe
import com.joe.jetpackdemo.db.repository.FavouriteShoeRepository
import com.joe.jetpackdemo.db.repository.ShoeRepository
import kotlinx.coroutines.launch

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * DetailModel
 * @author liujianming
 * @date 2023-02-15
 */
class DetailModel constructor(
    shoeRepository: ShoeRepository,
    private val favouriteShoeRepository: FavouriteShoeRepository,
    private val shoeId: Long,
    val userId: Long
) : ViewModel() {

    val shoe: LiveData<Shoe> = shoeRepository.getShoeById(shoeId)

    val favouriteShoe: LiveData<FavouriteShoe?> = favouriteShoeRepository.findFavouriteShoe(userId, shoeId)

    fun favourite() {
        viewModelScope.launch {
            favouriteShoeRepository.createFavouriteShoe(userId, shoeId)
        }
    }
}