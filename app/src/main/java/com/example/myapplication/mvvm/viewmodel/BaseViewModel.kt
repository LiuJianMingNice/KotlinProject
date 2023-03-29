package com.amazing.eye.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.mvvm.bean.BaseBean
import com.example.myapplication.mvvm.interfaces.IDatasListener
import com.example.myapplication.mvvm.model.BaseModel

open class BaseViewModel : ViewModel(), IDatasListener {
    var baseModel: BaseModel? = null
    private val successLiveData = MutableLiveData<Any>()
    private val errorLiveData = MutableLiveData<Any>()

    fun getSuccessLiveData(): MutableLiveData<Any> = successLiveData
    fun getErrorLiveData(): MutableLiveData<Any> = errorLiveData

    open fun onSuccess(any: Any) {
        successLiveData.value = any
    }

    open fun onFail(any: Any) {
        errorLiveData.value = any
    }

    open fun loadData() {
        baseModel?.let {
            it.setIDatasListener(this)
            it.loadData()
        }
    }

    override fun getSuccess(baseBean: BaseBean) {
        onSuccess(baseBean)
    }

    override fun getFaild(baseBean: BaseBean) {
        onFail(baseBean)
    }
}