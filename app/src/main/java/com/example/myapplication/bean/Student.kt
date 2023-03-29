package com.example.myapplication.bean

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Student
 * @author liujianming
 * @date 2021-12-21
 */
data class Student(override val id: Int, override val name: String, val grade: String): Person(id, name)