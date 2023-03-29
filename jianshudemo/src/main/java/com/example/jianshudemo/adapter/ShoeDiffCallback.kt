package com.example.jianshudemo.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.jianshudemo.dp.data.Shoe

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * ShoeDiffCallback
 * @author liujianming
 * @date 2023-02-16
 */
class ShoeDiffCallback: DiffUtil.ItemCallback<Shoe>() {
    override fun areItemsTheSame(oldItem: Shoe, newItem: Shoe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Shoe, newItem: Shoe): Boolean {
        return oldItem == newItem
    }
}