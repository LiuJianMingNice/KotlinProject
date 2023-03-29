package com.example.myapplication.mvvm.interfaces

import com.amazing.eye.viewmodel.BaseViewModel

interface IBaseView {

    fun <T : BaseViewModel> createViewModel(viewModelClass: Class<T>): T

    fun  registerViewModelObserver(baseViewModel: BaseViewModel)

    fun onApiSuccessCallBack(any: Any)

    fun onApiErrorCallBack(any: Any)
}