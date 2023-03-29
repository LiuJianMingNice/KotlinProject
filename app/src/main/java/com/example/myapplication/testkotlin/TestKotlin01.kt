package com.example.myapplication.testkotlin

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * TestKotlin01
 * @author liujianming
 * @date 2021-12-28
 */
class TestKotlin01 {
}

fun main() {
    payFoo {
        println("write kotlin")
    }
}

fun payFoo(block: () -> Unit) {
    println("before block")
    block()
    println("end block")
}