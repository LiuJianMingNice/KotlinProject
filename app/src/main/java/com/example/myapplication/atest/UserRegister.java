package com.example.myapplication.atest;

import com.google.gson.annotations.SerializedName;

public class UserRegister {
    public static class RequestParam {
        @SerializedName("Email")
        String email;

        @SerializedName("Password")
        String password;

        @SerializedName("ValidateCode")
        String validateCode;

        public RequestParam(String email, String password, String validateCode) {
            this.email = email;
            this.password = password;
            this.validateCode = validateCode;
        }
    }
}
