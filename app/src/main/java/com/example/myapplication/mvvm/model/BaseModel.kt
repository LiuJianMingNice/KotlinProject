package com.example.myapplication.mvvm.model

import com.amazing.eye.network.RestCreator
import com.example.myapplication.mvvm.bean.BaseBean
import com.example.myapplication.mvvm.interfaces.IDatasListener
import com.example.myapplication.mvvm.network.RestService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class BaseModel {

    abstract fun getObservable(): Observable<out BaseBean>

    protected fun getRestService(): RestService {
        return RestCreator.getInstance().getRestService()
    }

    fun loadData() {
        getObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(baseBean: BaseBean) {
                    baseBean.responCode = 1
                    baseBean.responType = ""
                    iDatasListener?.getSuccess(baseBean)
                }

                override fun onError(e: Throwable) {
                    val baseBean = BaseBean()
                    baseBean.responCode = -1
                    baseBean.responType = ""
                    baseBean.errorMessage = e.message.toString()
                    iDatasListener?.getFaild(baseBean)
                }

            })
    }

    private var iDatasListener: IDatasListener? = null

    fun setIDatasListener(iDatasListener: IDatasListener) {
        this.iDatasListener = iDatasListener
    }
}