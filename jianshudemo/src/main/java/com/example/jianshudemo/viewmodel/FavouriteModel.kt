package com.example.jianshudemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.jianshudemo.dp.data.Shoe
import com.joe.jetpackdemo.db.repository.ShoeRepository

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * FavouriteModel
 * @author liujianming
 * @date 2023-02-13
 */
class FavouriteModel constructor(shoeRepository: ShoeRepository, userId:Long) : ViewModel() {

    // 鞋子集合的观察类
    val shoes: LiveData<List<Shoe>> = shoeRepository.getShoesByUserId(userId)

}