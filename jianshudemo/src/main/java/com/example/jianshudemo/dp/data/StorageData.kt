package com.example.jianshudemo.dp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * StorageData
 * @author liujianming
 * @date 2023-02-07
 */

/**
 * 存储优化中用来测试的表
 */
@Entity(tableName = "storage_data")
data class StorageData(
    @PrimaryKey(autoGenerate = false) var id: String = "",
    @ColumnInfo(name = "storage_value") val value: String
)
