package com.example.myapplication.testkotlin

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Mathematics
 * @author liujianming
 * @date 2021-12-23
 */
sealed class Mathematics() {

    data class Dou(val number: Double) : Mathematics()
    data class Sub(val e1: Mathematics, val e2: Mathematics) : Mathematics()
    object NotANumber : Mathematics()

    fun eval(m: Mathematics): Double = when(m) {
        is Dou -> {
            m.number
        }
        is Sub -> eval(m.e1) - eval(m.e2)
        NotANumber -> Double.NaN
    }
}

fun main(args:Array<String>) {
    var ec1: Mathematics = Mathematics.Dou(5.0)
    var d1 = ec1.eval(ec1)
    println(d1)

    var ec2:Mathematics = Mathematics.Sub(ec1, Mathematics.Dou(3.0))
    var d2 = ec2.eval(ec2)
    println(d2)

    var ec3:Mathematics = Mathematics.NotANumber
    var d3 = ec3.eval(ec3)
    println(d3)
}