package com.example.myapplication.atestkotlin

import com.example.myapplication.atest.UserRegister
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * AccountManagerApi
 * @author liujianming
 * @date 2022-01-05
 */
interface AccountManagerApi {
    /**
     * 用户注册
     *
     *
     * 不需要鉴权
     *
     * @param param [UserRegister.RequestParam]
     * @return
     */
    @POST("common/api/User/Regist")
    fun register(@Body param: UserRegister.RequestParam?): Observable<ResponseBody?>?

    /**
     * 获取验证码
     *
     *
     * 不需要鉴权
     *
     * @param param
     * @return
     */
    @POST("common/api/User/GetValidateCode")
    fun getValidateCode(@Body param: UserGetValidateCode.RequestParam): Observable<ResponseBody?>?
}