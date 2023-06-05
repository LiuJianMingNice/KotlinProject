package com.example.flowtest.test

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * Flow
 * @author liujianming
 * @date 2023-05-05
 */
fun main() {
//    runBlocking {
//        testMap()
//    }
//    testLaunch()
//    testFilter()
//    testEach()
//    test()
//    testDebounce()
//    testSample()
//    testReduce()
//    testFold()
//    testFlatMapConcat()
    testZip()
}

suspend fun testMap() {
    val flow = flowOf(1, 2, 3, 4, 5)
    flow.map {
        it * it
    }.collect {
        println(it)
    }
}

fun testLaunch() {
    GlobalScope.launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    Thread.sleep(2000L)
}

fun testFilter() {
    runBlocking {
        val flow = flowOf(1, 2, 3, 4, 5)
        flow.filter {
            it % 2 == 0
        }.map {
            it * it
        }.collect {
            println(it)
        }
    }
}

fun testEach() {
    runBlocking {
        val flow = flowOf(1, 2, 3, 4, 5)
        flow.onEach {
            println(it)
        }.collect {

        }
    }
}

fun test() {
    runBlocking {
        val flow = flowOf(1, 2, 3, 4, 5)
        flow.filter {
            it % 2 == 0
        }.onEach {
            println(it)
        }.map {
            it * it
        }.collect {
            println("最后的结果： " + it)
        }
    }
}

fun testDebounce() {
    runBlocking {
        flow {
            emit(1)
            emit(2)
            delay(600)
            emit(3)
            delay(100)
            emit(4)
            delay(100)
            emit(5)
        }
            .debounce(500)
            .collect {
                println(it)
            }
    }
}

fun testSample() {
    runBlocking {
        flow {
            while (true) {
                emit("发送一条弹幕")
            }
        }
            .sample(1000)
            .flowOn(Dispatchers.IO)
            .collect {
                println(it)
            }
    }
}

fun testReduce() {
    runBlocking {
        val result = flow {
            for (i in (1..100)) {
                emit(i)
            }
        }.reduce { accumulator, value -> accumulator + value }
        println(result)
    }
}

fun testFold() {
    runBlocking {
        val result = flow {
            for (i in ('A'..'Z')) {
                emit(i.toString())
            }
        }.fold("Alphabet: ") { acc, value -> acc + value }
        println(result)
    }
}

fun testFlatMapConcat() {
    runBlocking {
        flowOf(1, 2, 3)
            .flatMapConcat { flowOf("a$it", "b$it") }
            .collect {
                println(it)
            }
    }
}

fun testZip() {
    runBlocking {
        val start = System.currentTimeMillis()
        val flow1 = flow {
            delay(3000)
            emit("a")
        }
        val flow2 = flow {
            delay(2000)
            emit(1)
        }
        flow1.zip(flow2) { a, b ->
            a + b
        }.collect{
            val end = System.currentTimeMillis()
            println("Time cost: ${end - start}ms")
        }
    }
}

fun sendRealtimeWeatherRequest(): Flow<String> = flow {
    emit("realtimeWeather")
}

fun sendSevenDaysWeatherRequest(): Flow<String> = flow {
    emit("sevenDaysWeather")
}

