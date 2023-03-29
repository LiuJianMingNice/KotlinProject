package com.example.jianshudemo.viewmodel

import android.content.Context
import com.example.jianshudemo.common.BaseConstant
import com.example.jianshudemo.dp.RepositoryProvider
import com.example.jianshudemo.dp.repository.UserRepository
import com.example.jianshudemo.utils.AppPrefsUtils
import com.example.jianshudemo.viewmodel.factory.*
import com.joe.jetpackdemo.db.repository.FavouriteShoeRepository
import com.joe.jetpackdemo.db.repository.ShoeRepository
import com.joe.jetpackdemo.db.repository.StorageDataRepository

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * CustomViewModelProvider
 * @author liujianming
 * @date 2023-02-13
 */
object CustomViewModelProvider {

    fun providerRegisterModel(context: Context): RegisterModelFactory {
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
        return RegisterModelFactory(repository)
    }

    fun providerLoginModel(context: Context): LoginModelFactory {
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
        return LoginModelFactory(repository, context)
    }

    fun providerShoeModel(context: Context): ShoeModelFactory {
        val repository: ShoeRepository = RepositoryProvider.providerShoeRepository(context)
        return ShoeModelFactory(repository)
    }

    fun providerFavouriteModel(context: Context): FavouriteModelFactory {
        val repository: ShoeRepository = RepositoryProvider.providerShoeRepository(context)
        val userId:Long = AppPrefsUtils.getLong(BaseConstant.SP_USER_ID)
        return FavouriteModelFactory(repository,userId)
    }

    fun providerMeModel(context: Context): MeModelFactory {
        val repository:UserRepository = RepositoryProvider.providerUserRepository(context)
        return MeModelFactory(repository)
    }

    /**
     * @shoeId 鞋子的Id
     * @userId 用户的Id
     */
    fun providerDetailModel(context: Context, shoeId: Long, userId: Long): FavouriteShoeModelFactory {
        val repository: ShoeRepository = RepositoryProvider.providerShoeRepository(context)
        val favShoeRepository: FavouriteShoeRepository = RepositoryProvider.providerFavouriteShoeRepository(context)
        return FavouriteShoeModelFactory(repository, favShoeRepository, shoeId, userId)
    }

    fun provideStorageDataModel(context: Context):StorageModelFactory{
        val repository: StorageDataRepository = RepositoryProvider.providerStorageDataRepository(context)
        return StorageModelFactory(repository)
    }
}