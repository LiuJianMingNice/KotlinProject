package com.example.jianshudemo.binding

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jianshudemo.R
import com.example.jianshudemo.common.listener.SimpleWatcher
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * ShoeBindingAdapter
 * @author liujianming
 * @date 2023-02-28
 */

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .asBitmap()
            .load(imageUrl)
            .placeholder(R.drawable.glide_placeholder)
            .centerCrop()
            .into(view)
    }
}

//加载带圆角的头像
@BindingAdapter("imageTransFromUrl")
fun bindImageTransFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCornersTransformation(
                        20,
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
            )
            .into(view)
    }
}

// 文本监听器
@BindingAdapter("addTextChangedListener")
fun addTextChangedListener(editText: EditText, simpleWatcher: SimpleWatcher) {
    editText.addTextChangedListener(simpleWatcher)
}