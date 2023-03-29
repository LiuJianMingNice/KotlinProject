package com.example.myapplication.customview

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.myapplication.R

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * CountdownButton
 * @author liujianming
 * @date 2021-12-30
 */
class CountdownButton(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatButton(context, attrs) {
    private val mHandler: Handler = Handler(Looper.myLooper()!!)
    private var mCountTime = 10
    private var mCountTimeCache = 10
    private var mCountTimeBtnText = ""
    private var mCountTimeEndText = ""


    init {
        if (attrs != null) {
            val countdown: TypedArray = getContext().theme.obtainStyledAttributes(attrs, R.styleable.CountdownButton, 0, 0)
            mCountTime = countdown.getInt(R.styleable.CountdownButton_countdown_time, 10)
            mCountTimeBtnText = countdown.getString(R.styleable.CountdownButton_countdown_time_btn_text).toString()
            mCountTimeEndText = countdown.getString(R.styleable.CountdownButton_countdown_time_end_text).toString()

        }
        mCountTimeCache = mCountTime
        this.text = mCountTimeBtnText
    }

    /*
           倒计时，并处理点击事件
        */
    fun sendVerifyCode() {
        mHandler.postDelayed(countDown, 0)
    }

    /*
        倒计时
     */
    private val countDown = object : Runnable {
        override fun run() {
            this@CountdownButton.text = mCountTime.toString() + "s "
            this@CountdownButton.background = ContextCompat.getDrawable(context, R.drawable.shape_dev_save_disable)
            this@CountdownButton.setTextColor(ContextCompat.getColor(context, R.color.white))
            this@CountdownButton.isEnabled = false

            if (mCountTime > 0) {
                mHandler.postDelayed(this, 1000)
            } else {
                resetCounter()
            }
            mCountTime--
        }
    }

    fun removeRunnable() {
        mHandler.removeCallbacks(countDown)
    }

    //重置按钮状态
    fun resetCounter(vararg text: String) {
        this.isEnabled = true
        if (text.isNotEmpty() && "" != text[0]) {
            this.text = text[0]
        } else {
            this.text = mCountTimeEndText
        }
        this.background = ContextCompat.getDrawable(context, R.drawable.shape_dev_save)
        this.setTextColor(ContextCompat.getColor(context, R.color.white))
        mCountTime = mCountTimeCache
    }
}