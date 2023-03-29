package com.example.myapplication.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.myapplication.base.BaseMultiTypeAdapter
import com.example.myapplication.bean.Person
import com.example.myapplication.bean.Student
import com.example.myapplication.bean.Teacher
import com.example.myapplication.databinding.ItemPersionBinding
import com.example.myapplication.databinding.ItemStudentBinding
import com.example.myapplication.databinding.ItemTeacherBinding

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * SecondAdapter
 * @author liujianming
 * @date 2021-12-21
 */
class SecondAdapter: BaseMultiTypeAdapter<Person>() {

    companion object{
        private const val ITEM_DEFAULT_TYPE = 0
        private const val ITEM_STUDENT_TYPE = 1
        private const val ITEM_TEACHER_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is Student -> ITEM_STUDENT_TYPE
            is Teacher -> ITEM_TEACHER_TYPE
            else -> ITEM_DEFAULT_TYPE
        }
    }

    override fun onCreateMultiViewHolder(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return when(viewType){
            ITEM_STUDENT_TYPE -> loadLayout(ItemStudentBinding::class.java,parent)
            ITEM_TEACHER_TYPE ->  loadLayout(ItemTeacherBinding::class.java,parent)
            else ->  loadLayout(ItemPersionBinding::class.java,parent)
        }
    }

    override fun MultiTypeViewHolder.onBindViewHolder(holder: MultiTypeViewHolder, item: Person, position: Int) {
        when(holder.binding){
            is ItemStudentBinding ->{
                Log.d("TestDataBinding","item : $item   position : $position")
            }
            is ItemTeacherBinding ->{
                Log.d("TestDataBinding","item : $item   position : $position")
            }
        }
    }

}