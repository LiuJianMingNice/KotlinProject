package com.example.myapplication.adapter

import com.example.myapplication.base.BaseAdapter
import com.example.myapplication.databinding.ItemHomeBinding
import com.example.myapplication.interfaces.ItemClickListener

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * HomeAdapter
 * @author liujianming
 * @date 2021-12-21
 */
class HomeAdapter(private val listener: ItemClickListener<String>): BaseAdapter<String, ItemHomeBinding>() {

    override fun ItemHomeBinding.setListener() {
        itemClickListener = listener
    }

    override fun ItemHomeBinding.onBindViewHolder(bean: String, position: Int) {
        this.bean = bean
        this.position = position
        tv.text = bean
    }
}