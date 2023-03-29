package com.example.jianshudemo.dp.datasource

import androidx.paging.PagingSource
import com.example.jianshudemo.dp.data.Shoe
import com.joe.jetpackdemo.db.repository.ShoeRepository
import java.lang.Exception

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * CustomPageDataSource
 * @author liujianming
 * @date 2023-02-09
 */

private const val SHOE_START_INDEX = 0

class CustomPageDataSource(private val shoeRepository: ShoeRepository): PagingSource<Int, Shoe>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Shoe> {
        val pos = params.key ?: SHOE_START_INDEX
        val startIndex = pos * params.loadSize + 1
        val endIndex = (pos + 1) * params.loadSize
        return try {
            Thread.sleep(5000)
            //从数据库拉取数据
            val shoes = shoeRepository.getPageShoes(startIndex = startIndex.toLong(), endIndex = endIndex.toLong())
            //返回你的分页结果，并填入前一页的key和后一页的key
            LoadResult.Page(shoes,
            if (pos <= SHOE_START_INDEX) null else pos - 1,
            if (shoes.isNullOrEmpty()) null else pos + 1)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}