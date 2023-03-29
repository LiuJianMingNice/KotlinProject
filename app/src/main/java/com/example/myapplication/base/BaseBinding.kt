package com.example.myapplication.base

import androidx.databinding.ViewDataBinding

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * BaseBinding
 * @author liujianming
 * @date 2021-12-21
 */
interface BaseBinding<VB: ViewDataBinding> {
    fun VB.initBinding()
}