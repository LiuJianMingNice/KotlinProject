package com.example.myapplication.customview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import com.example.myapplication.R
import com.example.myapplication.databinding.PtzViewBinding

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * PTZView
 * @author liujianming
 * @date 2022-01-18
 */
class PTZView(context: Context, attrs: AttributeSet?):
    RelativeLayout(context, attrs) {
    private lateinit var binding: PtzViewBinding

    init {
        build()
    }

    fun build(): PTZView {
        val root = View.inflate(context, R.layout.ptz_view, null)
        binding = PtzViewBinding.bind(root)
        return this
    }


}