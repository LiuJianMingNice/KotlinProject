package com.example.myapplication.testkotlin

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Aquarium1
 * @author liujianming
 * @date 2021-12-27
 */
open class Aquarium1(open var length: Int = 100, open var width: Int = 20, open var height: Int = 40) {
    open var volume: Int
        get() = width * height * length / 1000
        set(value) {
            height = (value * 1000) / (width * length)
        }

    open val shape = "rectangle"

    open var water: Double = 0.0
        get() = volume * 0.9

    fun printlnSize() {
        println(shape)
        println("Width: $width cm " +
                "Length: $length cm " +
                "Height: $height cm")
        println("Volume: $volume l Water: $water l (${water/volume*100.0}% full")
    }
}

fun buildAquarium1() {
    val aquarium6 = Aquarium1(length = 25, width = 25, height = 40)
    aquarium6.printlnSize()

    val myTower = TowerTank(diameter = 25, height = 40)
    myTower.printlnSize()
}

fun main() {
    buildAquarium1()
}