package com.example.jianshudemo.utils

import android.content.Context

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * UiUtils
 * @author liujianming
 * @date 2023-02-13
 */
object UiUtils {

    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}