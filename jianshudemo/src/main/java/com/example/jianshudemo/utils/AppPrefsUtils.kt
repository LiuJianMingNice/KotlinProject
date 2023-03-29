package com.example.jianshudemo.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.jianshudemo.common.BaseApplication
import com.example.jianshudemo.common.BaseConstant

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * AppPrefsUtils
 * @author liujianming
 * @date 2023-02-09
 */
object AppPrefsUtils {
    private var sp: SharedPreferences = BaseApplication.context.getSharedPreferences(BaseConstant.TABLE_PREFS, Context.MODE_PRIVATE)
    private var ed: SharedPreferences.Editor

    init {
        ed = sp.edit()
    }

    fun changeName(name: String) {
        sp = BaseApplication.context.getSharedPreferences(name, Context.MODE_PRIVATE)
        ed = sp.edit()
    }

    fun putBoolean(key: String, value: Boolean) {
        ed.putBoolean(key, value)
        ed.commit()
    }

    fun putBooleanWithNotCommit(key: String, value: Boolean) {
        ed.putBoolean(key, value)
//        ed.commit()
    }

    fun getBoolean(key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        ed.putString(key, value)
        ed.commit()
    }

    fun putStringWithNotCommit(key:String, value: String) {
        ed.putString(key, value)
//        ed.commit()
    }

    fun getString(key: String): String {
        return sp.getString(key, "")!!
    }

    /*
        Int数据
     */
    fun putInt(key: String, value: Int) {
        ed.putInt(key, value)
        ed.commit()
    }

    fun putIntWithNotCommit(key: String, value: Int) {
        ed.putInt(key, value)
    }

    /*
        默认 0
     */
    fun getInt(key: String): Int {
        return sp.getInt(key, 0)
    }

    /*
        Long数据
     */
    fun putLong(key: String, value: Long) {
        ed.putLong(key, value)
        ed.commit()
    }

    fun putLongWithNotCommit(key: String, value: Long) {
        ed.putLong(key, value)
        // ed.commit()
    }

    /*
        默认 0
     */
    fun getLong(key: String): Long {
        return sp.getLong(key, 0)
    }

    /*
        Long数据
     */
    fun putFloat(key: String, value: Float) {
        ed.putFloat(key, value)
        ed.commit()
    }

    fun putFloatWithNotCommit(key: String, value: Float) {
        ed.putFloat(key, value)
    }

    fun commit(){
        ed.commit()
    }

    /*
        默认 0
     */
    fun getFloat(key: String): Float {
        return sp.getFloat(key, 0f)
    }

    /*
        Set数据
     */
    fun putStringSet(key: String, set: Set<String>) {
        val localSet = getStringSet(key).toMutableSet()
        localSet.addAll(set)
        ed.putStringSet(key, localSet)
        ed.commit()
    }

    /*
        默认空set
     */
    fun getStringSet(key: String): Set<String> {
        val set = setOf<String>()
        return sp.getStringSet(key, set) as Set<String>
    }

    /*
        删除key数据
     */
    fun remove(key: String) {
        ed.remove(key)
        ed.commit()
    }

    fun clearAll(){
        ed.clear()
        ed.commit()
    }

}