package com.amazing.eye.bean

import android.os.Parcel
import android.os.Parcelable
import com.example.myapplication.mvvm.bean.BaseBean
import java.io.Serializable

data class VideoBean(
    var videoId: Int?,
    var feed: String?,
    var title: String?,
    var description: String?,
    var duration: Long?,
    var playUrl: String?,
    var category: String?,
    var blurred: String?,
    var collect: String?,
    var share: String?,
    var reply: String?,
    var time: Long,
    var isPlaying: Boolean,
    var currentPosition: Long
) : Serializable, BaseBean()