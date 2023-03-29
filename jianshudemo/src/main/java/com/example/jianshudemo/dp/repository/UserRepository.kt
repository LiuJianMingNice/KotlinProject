package com.example.jianshudemo.dp.repository

import androidx.lifecycle.LiveData
import com.example.jianshudemo.dp.dao.UserDao
import com.example.jianshudemo.dp.data.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * UserRepository
 * @author liujianming
 * @date 2023-02-07
 */
class UserRepository private constructor(private val userDao: UserDao) {

    /**
     * 获取所有的用户
     */
    fun getAllUsers() = userDao.getAllUsers()

    /**
     * 根据id选择用户
     */
    fun findUserById(id: Long): LiveData<User> = userDao.findUserById(id)

    /**
     * 登录用户
     */
    fun login(account: String, pwd: String): LiveData<User?> = userDao.login(account, pwd)

    /**
     * 更新用户
     */
    suspend fun updateUser(user: User) {
        withContext(IO) {
            userDao.updateUser(user)
        }
    }

    suspend fun register(email: String, account: String, pwd: String): Long {
        return withContext(IO) {
            userDao.insertUser(User(account, pwd, email, "https://raw.githubusercontent.com/mCyp/Photo/master/1560651318109.jpeg"))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userDao: UserDao): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userDao).also {
                instance = it
            }
        }
    }
}