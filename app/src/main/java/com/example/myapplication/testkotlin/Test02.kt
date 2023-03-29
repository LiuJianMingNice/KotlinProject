package com.example.myapplication.testkotlin


/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Test02
 * @author liujianming
 * @date 2021-12-23
 */
class Test02 {

}

fun main(args: Array<String>) {
//    test1()
//    test2()
    test3()
}

fun test1() {
    val numbers = intArrayOf(1,2,3)
    val numbers3 = intArrayOf(4,5,6)
    val foo2 = numbers + numbers3
    println(foo2[3])
}

fun test2() {
    val numbers = intArrayOf(1, 2, 3)
    val oceans = listOf("Atlantic", "Pacific")
    val oddList = listOf(numbers, oceans, "salmon")
    println(oddList)
}

fun test3() {
    val list = listOf(12, 34, 32, 52)
    var value = list.filter { it > 33 }
    println("value===>>> " + value)


}