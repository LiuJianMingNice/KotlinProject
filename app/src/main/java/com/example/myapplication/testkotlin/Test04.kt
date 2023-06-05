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
//    println("The answer is $answer")
//    println("sum ====>>> " + sum(2, 3))
    println("getString ===>>> " + getStringLength("hello"))
}

fun sum(a: Int, b: Int): Int {
    return a + b
}

fun maxOf(a: Int, b: Int) = if (a > b) a else b

fun getStringLength(obj: Any): Int? {
    if (obj is String && obj.length > 0) {
        return obj.length
    }
    return null
}

fun testFor() {
    val items = listOf("apple", "banana", "Kiwifruit")
    for (item in items) {
        println(item)
    }
}

fun testWhile() {
    val items = listOf("apple", "banana", "kiwifruit")
    var index = 0
    while (index < items.size) {
        println("item at $index is ${items[index]}")
        index++
    }
}

fun testWhen(obj: Any): String =
    when (obj) {
        1 -> "One"
        "Hello" -> "Greeting"
        is Long -> "Long"
        !is String -> "Not a string"
        else -> "UnKnown"
    }

fun testRange() {
    val x = 10
    val y = 9
    if (x in 1..y+1) {
        println("fits in range")
    }
}
