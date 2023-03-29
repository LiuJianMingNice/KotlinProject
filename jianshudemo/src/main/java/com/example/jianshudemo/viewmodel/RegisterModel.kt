package com.example.jianshudemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jianshudemo.dp.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * RegisterModel
 * @author liujianming
 * @date 2023-02-13
 */
class RegisterModel constructor(private val repository: UserRepository) : ViewModel() {

    val n = MutableLiveData("")
    val p = MutableLiveData("")
    val mail = MutableLiveData("")
    val enable = MutableLiveData(false)


    /**
     * 用户名改变回调的函数
     */
    fun onNameChanged(s: CharSequence) {
        n.value = s.toString()
        judgeEnable()
    }

    /**
     * 邮箱改变的时候
     */
    fun onEmailChanged(s: CharSequence) {
        mail.value = s.toString()
        judgeEnable()
    }

    /**
     * 密码改变的回调函数
     */
    fun onPwdChanged(s: CharSequence) {
        p.value = s.toString()
        judgeEnable()
    }

    private fun judgeEnable() {
        enable.value = p.value!!.isNotEmpty()
                && n.value!!.isNotEmpty()
                && mail.value!!.isNotEmpty()
    }

    fun register() {
        viewModelScope.launch {
            repository.register(mail.value!!, n.value!!, p.value!!)
        }
    }
}