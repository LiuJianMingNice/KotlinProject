package com.example.myapplication.testkotlin

import java.util.*


/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Test03
 * @author liujianming
 * @date 2021-12-24
 */
val decorations = listOf("rock", "pagoda", "plastic plant", "alligator", "flowerpot")
class Test03 {
}

fun main(args: Array<String>) {
//    test01()
//    feedTheFish()
//    swim()
//    swim("slow")
//    swim(speed="turtle-like")
//    println(decorations.filter { it[0] == 'p' })
//    var value = updateDirty(10, waterFilter)
//    println("value==>>> $value")
    var value = updateDirty(10, ::increaseDirty)
    println("value==>>> $value")
}

fun test01() {
    val isUnit = println("This is an expression")
    println(isUnit)
}

fun fishFood(day: String) : String {
    var food = ""
    when (day) {
        "Monday" -> food ="flakes"
        "Tuesday" -> food ="pellets"
        "Wednesday" -> food ="redworms"
        "Thursday" -> food ="granules"
        "Friday" -> food ="mosquitoes"
        "Saturday" -> food ="lettuce"
        "Sunday" -> food ="plankton"
    }
    return food
}

fun feedTheFish() {
    val day = randomDay()
    val food = fishFood(day)
    println("Today is $day and the fish eat $food")
}

fun randomDay(): String {
    val week = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    return week[Random().nextInt(week.size)]
}

fun swim(speed: String = "fast") {
    println("swimming $speed")
}

fun updateDirty(dirty: Int, operation: (Int) -> Int): Int {
    return operation(dirty)
}

val waterFilter: (Int) -> Int = {dirty -> dirty / 2}

fun increaseDirty(start: Int) = start + 1
