package com.example.jianshudemo.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.jianshudemo.common.BaseConstant
import com.example.jianshudemo.dp.RepositoryProvider
import com.example.jianshudemo.dp.data.Shoe
import com.example.jianshudemo.utils.AppPrefsUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * ShoeWorker
 * @author liujianming
 * @date 2023-02-13
 */
class ShoeWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val TAG by lazy {
        ShoeWorker::class.java.simpleName
    }

    //指定Dispatchers
    override val coroutineContext: CoroutineDispatcher
        get() = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open("shoes.json").use {
                JsonReader(it.reader()).use {
                    val shoeType = object : TypeToken<List<Shoe>>() {}.type
                    val shoeList: List<Shoe> = Gson().fromJson(it, shoeType)

                    val shoeDao = RepositoryProvider.providerShoeRepository(applicationContext)
                    shoeDao.insertShoes(shoeList)
                    for (i in 0..6) {
                        for (shoe in shoeList) {
                            shoe.id += shoeList.size
                        }
                        shoeDao.insertShoes(shoeList)
                    }
                    AppPrefsUtils.putBoolean(BaseConstant.IS_FIRST_LAUNCH,false)
                    Result.success()
                }

            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

}