package com.example.myapplication.generics

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * WaterSupply
 * @author liujianming
 * @date 2021-12-28
 */
open class WaterSupply(var needProcessing: Boolean) {
}

class TapWater : WaterSupply(true) {
    fun addChemicalCleaners() {
        needProcessing = false
    }
}

class FishStoreWater : WaterSupply(false) {
}

class LakeWater : WaterSupply(true) {
    fun filter() {
        needProcessing = false
    }
}