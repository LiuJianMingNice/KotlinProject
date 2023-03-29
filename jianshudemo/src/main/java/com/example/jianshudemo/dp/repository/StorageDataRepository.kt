package com.joe.jetpackdemo.db.repository

import com.example.jianshudemo.dp.dao.StorageDataDao
import com.example.jianshudemo.dp.data.StorageData

/**
 * StorageData 仓库
 */
class StorageDataRepository private constructor(private val storageDataDao: StorageDataDao) {

    /**
     * 获取所有的数据
     */
    fun getAllStorageData() = storageDataDao.getAllStorageData()

    fun insertAllStorageData(data: List<StorageData>) {
        storageDataDao.insertStorageData(data)
    }

    fun insertOneStorageData(data: StorageData){
        storageDataDao.insertStorageData(data)
    }

    fun findStorageDataById(id: String): StorageData? {
        return storageDataDao.findStorageDataByKey(id)
    }


    companion object {
        @Volatile
        private var instance: StorageDataRepository? = null

        fun getInstance(storageDataDao: StorageDataDao): StorageDataRepository =
            instance ?: synchronized(this) {
                instance
                    ?: StorageDataRepository(storageDataDao).also {
                    instance = it
                }
            }

    }
}