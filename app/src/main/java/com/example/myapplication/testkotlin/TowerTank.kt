package com.example.myapplication.testkotlin

import kotlin.math.PI

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * TowerTank
 * @author liujianming
 * @date 2021-12-27
 */
class TowerTank(override var height: Int, var diameter: Int): Aquarium1(height = height, width = diameter, length = diameter) {
    override var volume: Int
        get() = (width / 2 * length / 2 * height / 1000 * PI).toInt()
        set(value) {
            height = ((value * 1000 / PI) / (width / 2 * length / 2)).toInt()
        }

    override var water = volume * 0.8

    override val shape = "cylinder"
}