package com.example.myapplication.atestkotlin

import android.content.Context
//import com.example.myapplication.atest.ServiceGenerator
//import com.example.myapplication.atest.UsAnlianSSLUtils.getCaSslContext
import javax.net.ssl.SSLContext

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * AccountApiServiceGenerator
 * @author liujianming
 * @date 2022-01-05
 */
class AccountApiServiceGenerator {
    //    private static final String API_BASE_URL = "https://us.anlian.co/";
    private fun getSslContext(context: Context): SSLContext? {
        return UsAnlianSSLUtils().getCaSslContext(context)
    }

    /**
     * 不需要鉴权的接口
     *
     * @param context
     * @return
     */
    fun createService(context: Context): AccountManagerApi? {
        return ServiceGenerator().createService(
            OkHttpHelper().createHttpsClientBuilder(getSslContext(context)),
            AccountManagerApi::class.java, "https://us.anlian.co/"
        )
    }

}