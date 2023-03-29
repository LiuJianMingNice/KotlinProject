package com.example.myapplication.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

import android.R

import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import kotlin.math.roundToInt


/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * PasswordRulesView
 * @author liujianming
 * @date 2021-12-29
 */
@SuppressLint("AppCompatCustomView")
class UnderlineTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {

    var underlineHeight: Int = 0
    var underlineColor: Int
    var drawableLeftBitmapWidth: Int = 0

    init {
        underlineColor = 0

        if (attrs != null) {
            //获取自定义属性
            var underlineTypedArray = getContext().theme.obtainStyledAttributes(attrs, com.example.myapplication.R.styleable.UnderlineTextView,
                                    0, 0)
            //获取具体属性值
            val drawableLeft = underlineTypedArray.getDrawable(com.example.myapplication.R.styleable.UnderlineTextView_android_drawableLeft)
            if (drawableLeft != null) {
                val drawableLeftBitmap = Bitmap.createBitmap(drawableLeft!!.intrinsicWidth, drawableLeft!!.intrinsicHeight, Bitmap.Config.ARGB_8888)
                drawableLeftBitmapWidth = drawableLeftBitmap.width
            }
            underlineColor = underlineTypedArray.getColor(com.example.myapplication.R.styleable.UnderlineTextView_underline_color, textColors.defaultColor)
            underlineHeight = underlineTypedArray.getDimension(com.example.myapplication.R.styleable.UnderlineTextView_underline_height,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics))
                .toInt();

            underlineTypedArray.recycle()
        }

    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom + underlineHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = underlineColor
        canvas!!.drawRect(drawableLeftBitmapWidth.toFloat(), (height - underlineHeight).toFloat(),
            width.toFloat(), height.toFloat(), paint)
    }

}