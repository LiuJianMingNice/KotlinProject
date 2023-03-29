package com.example.jianshudemo.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jianshudemo.common.BaseApplication
import com.example.jianshudemo.common.listener.SimpleWatcher
import com.example.jianshudemo.dp.RepositoryProvider
import com.example.jianshudemo.dp.data.Shoe
import com.example.jianshudemo.dp.data.User
import com.example.jianshudemo.dp.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * LoginModel
 * @author liujianming
 * @date 2023-02-07
 */
class LoginModel constructor(private val repository: UserRepository): ViewModel() {

    val n = MutableLiveData("")
    val p = MutableLiveData("")
    val enable = MutableLiveData(false)

    /*val n = ObservableField<String>("")
    val p = ObservableField<String>("")*/
    //lateinit var lifecycleOwner: LifecycleOwner

    /**
     * 用户名改变回调的函数
     */
    fun onNameChanged(s: CharSequence) {
        n.value = s.toString()
        judgeEnable()
    }

    private fun judgeEnable() {
        enable.value = n.value!!.isNotEmpty() && p.value!!.isNotEmpty()
    }

    /**
     * 密码改变的回调函数
     */
    fun onPwdChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        p.value = s.toString()
        judgeEnable()
    }

    val nameWatcher = object: SimpleWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)

            n.value = s.toString()
            judgeEnable()
        }
    }

    val pwdWatcher = object : SimpleWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            p.value = s.toString()
            judgeEnable()
        }
    }

    fun login(): LiveData<User?>? {
        val pwd = p.value!!
        val account = n.value!!

        return repository.login(account, pwd)
    }

    fun onFirstLaunch(): String {
        val context = BaseApplication.context
        context.assets.open("shoes.json").use {
            JsonReader(it.reader()).use {
                val shoeType = object : TypeToken<List<Shoe>>(){}.type
                val shoeList: List<Shoe> = Gson().fromJson(it, shoeType)

                val shoeDao = RepositoryProvider.providerShoeRepository(context)
                shoeDao.insertShoes(shoeList)
                for (i in 0..2) {
                    for(shoe in shoeList) {
                        shoe.id += shoeList.size
                    }
                    shoeDao.insertShoes(shoeList)
                }
            }
        }
        return "初始化数据成功"
    }

}