package com.example.myapplication.testkotlin

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Shark
 * @author liujianming
 * @date 2021-12-27
 */
class Shark: AquariumFish(), FishAction {
    override val color: String = "gray"
    override fun eat() {
        println("hunt and eat fish")
    }
}