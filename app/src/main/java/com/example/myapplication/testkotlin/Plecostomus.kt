package com.example.myapplication.testkotlin

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Plecostomus
 * @author liujianming
 * @date 2021-12-27
 */
class Plecostomus: FishColor, FishAction {
    override val color: String = "gold"
    override fun eat() {
        println("eat algae")
    }
}