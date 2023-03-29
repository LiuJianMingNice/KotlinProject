package com.example.myapplication.generics

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Aquarium
 * @author liujianming
 * @date 2021-12-28
 */
class Aquarium<out T: WaterSupply>(val waterSupply: T) {
//    fun addWater() {
//        check(!waterSupply.needProcessing) {
//            "water supply needs processing first"
//        }
//        println("adding water from $waterSupply")
//    }

    fun addWater(cleaner: Cleaner<T>) {
        if (waterSupply.needProcessing) {
            cleaner.clean(waterSupply)
        }
        println("water added")
    }
}

interface Cleaner<in T: WaterSupply> {
    fun clean(waterSupply: T)
}

class TapWaterCleaner : Cleaner<TapWater> {
    override fun clean(waterSupply: TapWater) = waterSupply.addChemicalCleaners()
}

fun addItemTo(aquarium: Aquarium<WaterSupply>) = println("item added")

fun isWaterClean(aquarium: Aquarium<WaterSupply>) {
    println("aquarium water is clean: ${!aquarium.waterSupply.needProcessing}")
}

fun genericsExample() {
//    val aquarium = Aquarium(WaterSupply.TapWater())
//    println("water needs Processing: ${aquarium.waterSupply.needProcessing}")
//    aquarium.waterSupply.addChemicalCleaners()
//    println("water needs Processing: ${aquarium.waterSupply.needProcessing}")

//    val aquarium2 = Aquarium("String")
//    println(aquarium2.waterSupply)

//    val aquarium3 = Aquarium(LakeWater())
//    aquarium3.waterSupply.filter()
//    aquarium3.addWater()

//    val aquarium4 = Aquarium(WaterSupply.TapWater())
//    addItemTo(aquarium4)

//    val cleaner = TapWaterCleaner()
//    val aquarium = Aquarium(WaterSupply.TapWater())
//    aquarium.addWater(cleaner)

    val aquarium = Aquarium(TapWater())
    isWaterClean(aquarium)
}

fun main() {
    genericsExample()
}

