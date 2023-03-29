package com.example.jianshudemo.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.jianshudemo.common.BaseConstant.KEY_IMAGE_URI
import com.example.jianshudemo.utils.makeStatusNotification
import java.text.SimpleDateFormat
import java.util.*

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * SaveImageToFileWorker
 * @author liujianming
 * @date 2023-02-13
 */
class SaveImageToFileWorker(ctx: Context, parameters: WorkerParameters) : Worker(ctx, parameters) {
    private val TAG by lazy {
        SaveImageToFileWorker::class.java.simpleName
    }
    private val Title = "Blurred Image"
    private val dateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )

    override fun doWork(): Result {
        // Makes a notification when the work starts and slows down the work so that
        // it's easier to see each WorkRequest start, even on emulated devices
        makeStatusNotification("Saving image", applicationContext)
        //sleep()

        val resolver = applicationContext.contentResolver
        return try {
            //获取外部传入的参数
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val bitmap =
                BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            val imageUrl = MediaStore.Images.Media.insertImage(
                resolver, bitmap, Title, dateFormatter.format(Date()))
            if (!imageUrl.isNullOrEmpty()) {
                val output = workDataOf(KEY_IMAGE_URI to imageUrl)

                Result.success()
            } else {
                Log.e(TAG, "Writing to MediaStore failed")
                Result.failure()
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to save image to Gallery", exception)
            Result.failure()
        }
    }
}