package com.example.myapplication.atestkotlin

import android.text.TextUtils
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Credentials.basic
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException
import java.util.*

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * ServiceGenerator
 * @author liujianming
 * @date 2022-01-05
 */
class ServiceGenerator {

    private val TAG = "ServiceGenerator"

    @Volatile
    private var sBuilder: Retrofit.Builder? = null

    private fun getRetrofitBuilder(): Retrofit.Builder? {
        if (sBuilder == null) {
            synchronized(Retrofit.Builder::class.java) {
                if (sBuilder == null) {
                    sBuilder = createBuilder()
                }
            }
        }
        return sBuilder
    }

    /**
     * 创建"基本配置"的 Retrofit.Builder, 只能被 [.getRetrofitBuilder] 使用
     */
    private fun createBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }

    /**
     * 无需鉴权认证的接口
     */
    fun <S> createService(
        clientBuilder: OkHttpClient.Builder,
        serviceClass: Class<S>,
        baseUrl: String?
    ): S {
        val httpClientBuilder: OkHttpClient.Builder = clientBuilder.addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Accept", "application/json")
                .header("X-RS-Request-ID", UUID.randomUUID().toString())
                .method(original.method, original.body)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        })
        return getRetrofitBuilder()
            ?.baseUrl(baseUrl)
            ?.client(httpClientBuilder.build())
            ?.build()
            ?.create(serviceClass)!!
    }

    /**
     * 认证(Authentication)相关的 createService(...) 方法 ↓
     *
     *
     * Basic Authentication on Android
     * {a http://blog.csdn.net/kiwi_coder/article/details/28677651}
     */
//    fun <S> createService(
//        clientBuilder: OkHttpClient.Builder,
//        serviceClass: Class<S>?,
//        baseUrl: String?,
//        username: String?,
//        password: String?
//    ): S {
//        return if (username != null && password != null) ({
//            val basic = basic(username, password)
//            val httpClientBuilder: OkHttpClient.Builder = clientBuilder.addInterceptor(Interceptor { chain ->
//                val original = chain.request()
//                val requestBuilder: Request.Builder = original.newBuilder()
//                    .header("Accept", "application/json")
//                    .header("Authorization", basic)
//                    .header("X-RS-Request-ID", UUID.randomUUID().toString())
//                    .method(original.method, original.body)
//                val request: Request = requestBuilder.build()
//                chain.proceed(request)
//            })
//            getRetrofitBuilder()
//                ?.baseUrl(baseUrl)
//                ?.client(httpClientBuilder.build())
//                ?.build()
//                ?.create(serviceClass)
//        })!! else {
//            throw IllegalArgumentException("'username' and 'password' must not null!")
//        }
//    }

    /**
     * Token Authentication on Android
     * {a http://blog.csdn.net/kiwi_coder/article/details/28677651}
     */
//    fun <S> createService(
//        clientBuilder: OkHttpClient.Builder,
//        serviceClass: Class<S>?,
//        baseUrl: String?,
//        authToken: String?
//    ): S {
//        return if (!TextUtils.isEmpty(authToken)) ({
//            val httpClientBuilder: OkHttpClient.Builder = clientBuilder.addInterceptor(Interceptor { chain ->
//                val original = chain.request()
//
//                // Request customization: add request headers
//                val requestBuilder: Request.Builder = original.newBuilder()
//                    .header("Accept", "application/json")
//                    .header("Authorization", authToken!!)
//                    .header("X-RS-Request-ID", UUID.randomUUID().toString())
//                    .method(original.method, original.body)
//                val request: Request = requestBuilder.build()
//                chain.proceed(request)
//            })
//            getRetrofitBuilder()
//                ?.baseUrl(baseUrl)
//                ?.client(httpClientBuilder.build())
//                ?.build()
//                ?.create(serviceClass)
//        })!! else {
//            throw IllegalArgumentException("'authToken' must not empty!")
//        }
//    }

    /**
     * OAuth on Android
     * {a http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html}
     * {a https://futurestud.io/blog/oauth-2-on-android-with-retrofit}
     */
//    fun <S> createService(
//        clientBuilder: OkHttpClient.Builder,
//        serviceClass: Class<S>?,
//        baseUrl: String?,
//        token: AccessToken?
//    ): S {
//        return if (token != null) ({
//            val httpClientBuilder: OkHttpClient.Builder = clientBuilder.addInterceptor(Interceptor { chain ->
//                val original = chain.request()
//                val requestBuilder: Request.Builder = original.newBuilder()
//                    .header("Accept", "application/json")
//                    .header(
//                        "Authorization",
//                        token.tokenType + " " + token.accessToken
//                    )
//                    .header("X-RS-Request-ID", UUID.randomUUID().toString())
//                    .method(original.method, original.body)
//                val request: Request = requestBuilder.build()
//                chain.proceed(request)
//            })
//            getRetrofitBuilder()
//                ?.baseUrl(baseUrl)
//                ?.client(httpClientBuilder.build())
//                ?.build()
//                ?.create(serviceClass)
//        })!! else {
//            throw IllegalArgumentException("'token' must not empty!")
//        }
//    }

    class AccessToken {
        var accessToken: String? = null
        private var mTokenType: String? = null

        // OAuth requires uppercase Authorization HTTP header value for token type
        var tokenType: String?
            get() {
                // OAuth requires uppercase Authorization HTTP header value for token type
                if (!Character.isUpperCase(mTokenType!![0])) {
                    mTokenType = Character.toString(mTokenType!![0])
                        .toUpperCase() + mTokenType!!.substring(1)
                }
                return mTokenType
            }
            set(tokenType) {
                mTokenType = tokenType
            }

    }
}