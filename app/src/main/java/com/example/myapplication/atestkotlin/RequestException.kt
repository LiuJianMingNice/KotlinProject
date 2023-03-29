package com.example.myapplication.atestkotlin

import java.io.IOException

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * RequestException
 * @author liujianming
 * @date 2022-01-05
 */
class RequestException(code: String, message: String) : IOException() {
    private var mCode: String? = null

//    fun RequestException(code: String?) {
//        super@RequestException
//        mCode = code
//    }
//
//    fun RequestException(code: String?, message: String?) {
////        super(message)
//        mCode = code
//    }

    init {
        mCode = code
    }

//    constructor(code: String) : this() {
//        mCode = code
//    }

//    constructor(code: String, message: String) : super(message) {
//    }


    fun getCode(): String? {
        return mCode
    }
}

class Person2(var name: String, var age: Int) {
    init {
        println("main constructor init")
    }

    constructor(name: String, age: Int, sex: String) : this(name, age) {

    }

    constructor(name: String, age: Int, sex: String, idCard: String) : this(name, age, sex) {

    }
}