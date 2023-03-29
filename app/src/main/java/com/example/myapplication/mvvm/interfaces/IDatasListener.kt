package com.example.myapplication.mvvm.interfaces

import com.example.myapplication.mvvm.bean.BaseBean

interface IDatasListener {

    fun getSuccess(baseBean: BaseBean)

    fun getFaild(baseBean: BaseBean)
}