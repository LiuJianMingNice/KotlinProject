package com.example.myapplication.util

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * ViewModelUtils
 * @author liujianming
 * @date 2021-12-22
 */
object ViewModelUtils {
    fun <VM: ViewModel> createViewModel(activity: ComponentActivity, factory: ViewModelProvider.Factory? = null, position: Int) : VM {
        val vbClass = (activity.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<*>>()
        val viewModel = vbClass[position] as Class<VM>
        return factory?.let {
            ViewModelProvider(activity, factory).get(viewModel)
        }?:let {
            ViewModelProvider(activity).get(viewModel)
        }
    }
}