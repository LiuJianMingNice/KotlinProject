package com.example.myapplication.testkotlin

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Decoration
 * @author liujianming
 * @date 2021-12-27
 */
data class Decoration(val rocks: String) {
}

fun makeDecorations() {
    val decoration1 = Decoration("granite")
    println(decoration1)
}

fun main() {
    makeDecorations()

    val list1 = listOf(1, 2, 3)
    val list2 = listOf("a", "bbb", "cc")
    println("list1==>> " + list1.sum())
}