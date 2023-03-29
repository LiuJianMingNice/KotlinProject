package com.example.myapplication.atestkotlin

import android.os.Build
import android.os.LocaleList
import java.lang.UnsupportedOperationException
import java.util.*

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * LanguageUtil
 * @author liujianming
 * @date 2022-01-05
 */
class LanguageUtil {

    private fun LanguageUtil() {
        throw UnsupportedOperationException("you can't instantiate me...")
    }

    /**
     * 判断系统语言 返回en-US(也可能是en-XX)表示英语,zh-CN表示简体中文,zh-TW表示繁体中文,以此类推
     * 兼容7.0
     */
    fun getLocaleLanguage(): String? {
        val locale: Locale
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault()[0]
        } else {
            Locale.getDefault()
        }
        return String.format("%s-%s", locale.language, locale.country)
    }

    /**
     * 获取发送给网页的系统语言
     *
     * @return
     */
    fun getSysLanguageSend2Web(): String? {
        val locale: Locale
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault()[0]
        } else {
            Locale.getDefault()
        }
        return if ("zh" == locale.language) {
            if ("CN" == locale.country) {
                //中文简体
                "zh-Hans"
            } else {
                //中文繁体
                "zh-Hant"
            }
        } else locale.language
    }

    /**
     * SmartCamLite 仅支持韩语和英语
     */
    fun getSystemLanguageForSendEmail(): String? {
        val locale: Locale
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault()[0]
        } else {
            Locale.getDefault()
        }
        return if ("ko" != locale.language) {
            "en"
        } else locale.language
    }

    /**
     * SmartCamLite 仅支持韩语和英语
     */
    fun getSystemLanguageForPush(): String? {
        return getSystemLanguageForSendEmail()
    }
}