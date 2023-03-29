package com.example.myapplication.interfaces

import android.view.View

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * ItemClickListener
 * @author liujianming
 * @date 2021-12-21
 */
interface ItemClickListener<T> {
    fun onItemClick(view: View, position: Int, data: T){}
    fun onItemClick(view: View, data: T){}
}