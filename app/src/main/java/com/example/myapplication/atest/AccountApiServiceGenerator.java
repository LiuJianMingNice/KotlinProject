package com.example.myapplication.atest;

import android.content.Context;

import javax.net.ssl.SSLContext;

/**
 * @author liuaibin
 */
public class AccountApiServiceGenerator {
//    private static final String API_BASE_URL = "https://us.anlian.co/";

    private static SSLContext getSslContext(Context context) {
        return UsAnlianSSLUtils.getCaSslContext(context);
    }

    /**
     * 不需要鉴权的接口
     *
     * @param context
     * @return
     */
    public static AccountManagerApi createService(Context context) {
        return ServiceGenerator.createService(OkHttpHelper.createHttpsClientBuilder(getSslContext(context)),
                AccountManagerApi.class, "https://us.anlian.co/");
    }


}
