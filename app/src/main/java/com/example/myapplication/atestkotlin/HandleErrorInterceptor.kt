package com.example.myapplication.atestkotlin

import android.util.Log
import com.alibaba.fastjson.JSON
import okhttp3.Response

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * HandleErrorInterceptor
 * @author liujianming
 * @date 2022-01-05
 */
class HandleErrorInterceptor : AbstractResponseBodyInterceptor() {

    private val TAG = "HandleErrorInterceptor"

    @Throws(RequestException::class)
    override fun intercept(response: Response, url: String?, body: String?): Response {
        if (response.code == 400) {
            val jsonObject = JSON.parseObject(body)
            if (jsonObject.containsKey("errorCode")) {
                val errorCode = jsonObject.getString("errorCode")
                if (HttpErrorCode().EMAIL_HAS_REGIST == errorCode || HttpErrorCode().VALIDATE_CODE_INVALID == errorCode || HttpErrorCode().NOT_SUCH_EMAIL == errorCode || HttpErrorCode().NOT_SUCH_USER == errorCode || HttpErrorCode().DEVICE_HAS_ADD == errorCode || HttpErrorCode().DEVICES_NOT_BELONG_THE_USER == errorCode) {
                    throw RequestException(errorCode, "")
                } else if (HttpErrorCode().INTERVAL == errorCode || HttpErrorCode().INVALID_PARAM == errorCode || HttpErrorCode().INVALID_TOKEN == errorCode) {
                    val errorMessage = jsonObject.getString("errorMessage")
                    throw RequestException(errorCode, errorMessage)
                } else {
//                    Log.e(TAG, "UnKnown errorCode [%s], body: %s", errorCode, body);
                }
            } else {
//                Log.e(TAG, "UnKnown error, body: %s", body);
            }
        } else if (response.code == 401) {
            Log.e(TAG, "Got en 401 Error")
            throw RequestException(HttpErrorCode().UNAUTHORIZED, "")
        } else if (response.code == 500) {
            throw RequestException(HttpErrorCode().INTERNAL_SERVER_ERROR, "Internal Server Error")
        }
        return response
    }
}