package com.example.myapplication.testkotlin

import com.example.myapplication.bean.Account
import java.io.File

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Test01
 * @author liujianming
 * @date 2021-12-23
 */

fun main(args: Array<String>) {
//    test()
//    testMapper()
//    test02()
    KotlinClass.doWork()
}

fun test() {
    val stringLengthFunc: (String) -> Int = {input ->
        input.length
    }
    val stringLength: Int = stringLengthFunc("Android")
    print("stringLength===>> " + stringLength)
}

fun stringMapper(str: String, mapper: (String) -> Int): Int {
    return mapper(str)
}

fun testMapper() {
    val stringLength = stringMapper("Android", { input ->
        input.length
    })
    println("stringLength===>>> " + stringLength)
}

fun test02() {//
    val account = Account("name", "Type")
    val accountName = account.name!!.trim()
    println("accountName===>>> " + accountName)



}

class Test01 {

    private var _table: Map<String, Int>? = null
    val table: Map<String, Int>
        get() {
            if (_table == null) {
                _table = HashMap()
            }
            return _table ?: throw AssertionError()
        }

}