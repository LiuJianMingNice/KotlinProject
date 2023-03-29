package com.example.jianshudemo.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.jianshudemo.common.BaseConstant.OUTPUT_PATH
import com.example.jianshudemo.utils.makeStatusNotification
import java.io.File
import java.lang.Exception

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * CleanUpWorker
 * @author liujianming
 * @date 2023-02-13
 */
class CleanUpWorker (ctx: Context, params: WorkerParameters) : Worker(ctx, params){
    override fun doWork(): Result {
        // Makes a notification when the work starts and slows down the work so that
        // it's easier to see each WorkRequest start, even on emulated devices
        makeStatusNotification("Cleaning up old temporary files", applicationContext)
        //sleep()

        return try {
            //删除逻辑
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entry.name
                            Log.i("CleanUpWorker", String.format("Delete %s - %s", name, deleted))
                        }
                    }
                }
            }
            Result.success()
        } catch (exception: Exception) {
            Log.e("CleanUpWorker", "Error cleaning up", exception)
            // 失败时返回
            Result.failure()
        }
    }
}