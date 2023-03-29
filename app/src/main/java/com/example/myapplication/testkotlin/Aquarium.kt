package com.example.myapplication.testkotlin

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Aquarium
 * @author liujianming
 * @date 2021-12-27
 */
class Aquarium(length: Int = 100, width: Int = 20, height: Int = 40) {
    var width: Int = width
    var height: Int = height
    var length: Int = length

    var volume: Int
        get() = width * height * length / 1000
        set(value) {
            height = (value * 1000) / (width * length)
        }

    init {
        println("aquarium initializing")
    }

    init {
        println("Volume: ${width * length * height / 1000} l")
    }

    constructor(numberOfFish: Int) : this() {
        val tank = numberOfFish * 2000 * 1.1
        height = (tank / (length * width)).toInt()
    }

    fun printSize() {
        println("Width: $width cm " +
               "Length: $length cm " +
                "Height: $height cm")

        println("Volume: $volume l")
    }
}

fun buildAquarium() {
//    val aquarium1 = Aquarium()
//    aquarium1.printSize()
//    aquarium1.height = 60
//    aquarium1.printSize()
//
//    val aquarium2 = Aquarium(width = 25)
//    aquarium2.printSize()
//
//    val aquarium3 = Aquarium(height = 35, length = 110)
//    aquarium3.printSize()
//
//    val aquarium4 = Aquarium(width = 25, height = 35, length = 110)
//    aquarium4.printSize()

    val aquarium5 = Aquarium(numberOfFish = 29)
    aquarium5.printSize()
    aquarium5.volume = 70
    aquarium5.printSize()
}

fun main() {
    buildAquarium()
}