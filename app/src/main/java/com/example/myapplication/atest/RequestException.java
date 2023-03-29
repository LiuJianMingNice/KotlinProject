package com.example.myapplication.atest;

import java.io.IOException;

/**
 * @author liuaibin
 */
public class RequestException extends IOException {
    private String mCode;

    public RequestException(String code) {
        super();
        mCode = code;
    }

    public RequestException(String code, String message) {
        super(message);
        mCode = code;
    }

    public String getCode() {
        return mCode;
    }
}
