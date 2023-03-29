package com.example.jianshudemo.dp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.jianshudemo.common.BaseConstant
import com.example.jianshudemo.dp.dao.FavouriteShoeDao
import com.example.jianshudemo.dp.dao.UserDao
import com.example.jianshudemo.dp.data.FavouriteShoe
import com.example.jianshudemo.dp.data.Shoe
import com.example.jianshudemo.dp.data.StorageData
import com.example.jianshudemo.dp.data.User
import com.example.jianshudemo.utils.AppPrefsUtils
import com.example.jianshudemo.dp.dao.ShoeDao
import com.example.jianshudemo.dp.dao.StorageDataDao
import com.example.jianshudemo.worker.ShoeWorker

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * AppDataBase
 * @author liujianming
 * @date 2023-02-07
 */

/**
 * 数据库文件
 */
@Database(entities = [User::class,Shoe::class, FavouriteShoe::class, StorageData::class],version = 2,exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase:RoomDatabase() {
    // 得到UserDao
    abstract fun userDao(): UserDao
    // 得到ShoeDao
    abstract fun shoeDao(): ShoeDao
    // 得到FavouriteShoeDao
    abstract fun favouriteShoeDao(): FavouriteShoeDao
    // 得到StorageDataDao
    abstract fun storageDataDao(): StorageDataDao

    companion object{
        @Volatile
        private var instance:AppDataBase? = null

        fun getInstance(context: Context):AppDataBase{
            return instance?: synchronized(this){
                instance?:buildDataBase(context)
                    .also {
                        instance = it
                    }
            }
        }

        private fun buildDataBase(context: Context):AppDataBase{
            return Room
                .databaseBuilder(context,AppDataBase::class.java,"jetPackDemo-database")
                .addCallback(object :RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        val isFirstLaunch = AppPrefsUtils.getBoolean(BaseConstant.IS_FIRST_LAUNCH)
                        if(isFirstLaunch){
                            // 读取鞋的集合
                            val request = OneTimeWorkRequestBuilder<ShoeWorker>().build()
                            WorkManager.getInstance().enqueue(request)
                        }

                    }
                })
                .build()
        }
    }
}