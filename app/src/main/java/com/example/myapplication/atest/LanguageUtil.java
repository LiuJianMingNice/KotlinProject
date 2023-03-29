package com.example.myapplication.atest;

import android.app.Activity;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;

import java.util.Locale;

public class LanguageUtil {

    private LanguageUtil() {
        throw new UnsupportedOperationException("you can't instantiate me...");
    }

    /**
     * 判断系统语言 返回en-US(也可能是en-XX)表示英语,zh-CN表示简体中文,zh-TW表示繁体中文,以此类推
     * 兼容7.0
     */
    public static String getLocaleLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return String.format("%s-%s", locale.getLanguage(), locale.getCountry());
    }

    /**
     * 获取发送给网页的系统语言
     *
     * @return
     */
    public static String getSysLanguageSend2Web() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        if ("zh".equals(locale.getLanguage())) {
            if ("CN".equals(locale.getCountry())) {
                //中文简体
                return "zh-Hans";
            } else {
                //中文繁体
                return "zh-Hant";
            }
        }
        return locale.getLanguage();
    }

    /**
     * SmartCamLite 仅支持韩语和英语
     */
    public static String getSystemLanguageForSendEmail() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        if (!"ko".equals(locale.getLanguage())) {
            return "en";
        }
        return locale.getLanguage();
    }

    /**
     * SmartCamLite 仅支持韩语和英语
     */
    public static String getSystemLanguageForPush() {
        return getSystemLanguageForSendEmail();
    }



}
