package com.example.myapplication.atestkotlin

import com.example.myapplication.atest.LanguageUtil
import com.google.gson.annotations.SerializedName

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * UserGetValidateCode
 * @author liujianming
 * @date 2022-01-05
 */
class UserGetValidateCode {
    class RequestParam(@field:SerializedName("Email") var email: String) {
        @SerializedName("Language")
        var language: String

        init {

            //获取系统语言
            language = LanguageUtil.getSystemLanguageForSendEmail()
        }
    }
}