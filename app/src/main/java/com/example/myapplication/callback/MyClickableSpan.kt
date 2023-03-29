package com.example.myapplication.callback

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * MyClickableSpan
 * @author liujianming
 * @date 2022-01-06
 */
class MyClickableSpan(text: String?) : ClickableSpan() {

    private var text: String? = null

    init {
        this.text = text
    }

    override fun onClick(p0: View) {

        Toast.makeText(p0.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.setColor(Color.parseColor("#3c78d8"))
        ds.setUnderlineText(true)
    }
}