package com.example.myapplication.atestkotlin

import com.google.gson.annotations.SerializedName

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * UserRegister
 * @author liujianming
 * @date 2022-01-05
 */
class UserRegister {
    class RequestParam(
        @field:SerializedName("Email") var email: String, @field:SerializedName(
            "Password"
        ) var password: String, @field:SerializedName("ValidateCode") var validateCode: String
    )
}