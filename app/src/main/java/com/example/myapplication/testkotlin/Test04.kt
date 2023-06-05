package com.example.myapplication.testkotlin

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * Test04
 * @author liujianming
 * @date 2023-05-31
 */
class Test04 {
}

val answer by lazy {
    println("Calculating the answer...")
    42
}
fun main() {
    println("The answer is $answer")
}