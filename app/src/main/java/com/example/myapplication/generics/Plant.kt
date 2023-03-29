package com.example.myapplication.generics

import java.util.*

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Plant
 * @author liujianming
 * @date 2021-12-28
 */

class Plant {
    fun trim(){}
    fun fertilize(){}
}
fun testAnnotations() {
    val classObj = Class.forName("com.example.myapplication.generics.Plant")

    for (m in classObj.declaredMethods) {
        println(m.name)
    }
}

fun labels() {
    outerLoop@ for (i in 1..100) {
        print("$i ")
        for (j in 1.. 100) {
            if (j in 1.. 100) {
                if (i > 10) break@outerLoop  //breaks to outer loop
            }
        }
    }
}

val waterFilter = {dirty: Int -> dirty / 2}

data class Fish(val name: String)

val myFish = listOf(Fish("Flipper"), Fish("Moby Dick"), Fish("Dory"))
//
fun fishExamples() {
    val fish = Fish("splashy")  //all lowercase
    with(fish.name) {
        println(replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
    }
//    replaceFirstChar
}

fun main() {
//    testAnnotations()
//    labels()
//    print(waterFilter(30))
//    println(myFish.filter { it.name.contains("i") })
    fishExamples()
}

