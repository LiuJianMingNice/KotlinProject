package com.example.myapplication.bean

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * Teacher
 * @author liujianming
 * @date 2021-12-21
 */
data class Teacher(override val id: Int, override val name: String, val subject: String): Person(id, name)