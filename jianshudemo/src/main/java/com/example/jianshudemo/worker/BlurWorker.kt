package com.example.jianshudemo.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.jianshudemo.common.BaseConstant
import com.example.jianshudemo.common.BaseConstant.KEY_IMAGE_URI
import com.example.jianshudemo.utils.blurBitmap
import com.example.jianshudemo.utils.makeStatusNotification
import com.example.jianshudemo.utils.writeBitmapToFile

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * BlurWorker
 * @author liujianming
 * @date 2023-02-13
 */
class BlurWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private var TAG: String = this::class.java.simpleName

    override fun doWork(): Result {
        val context = applicationContext
        val resourceUri = inputData.getString(BaseConstant.KEY_IMAGE_URI)

        //通知开始处理图片
        makeStatusNotification("Blurring image", context)

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = context.contentResolver
            val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            //创建Bitmap文件
            val output = blurBitmap(picture, context)
            //存入路径
            val outputUri = writeBitmapToFile(context, output)

            //输出路径
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            makeStatusNotification("Output is $outputUri", context)
            Result.success()
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur", throwable)
            Result.failure()
        }
    }
}