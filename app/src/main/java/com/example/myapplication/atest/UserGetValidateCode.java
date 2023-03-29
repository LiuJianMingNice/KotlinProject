package com.example.myapplication.atest;

import com.google.gson.annotations.SerializedName;

public class UserGetValidateCode {
    public static class RequestParam {
        @SerializedName("Email")
        String email;
        @SerializedName("Language")
        String language;

        public RequestParam(String email) {
            this.email = email;

            //获取系统语言
            language = LanguageUtil.getSystemLanguageForSendEmail();
        }
    }
}
