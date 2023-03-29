package com.example.myapplication.atestkotlin

import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_256_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_256_GCM_SHA384
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import java.lang.AssertionError
import java.lang.IllegalStateException
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * OkHttpHelper
 * @author liujianming
 * @date 2022-01-05
 */
class OkHttpHelper {

    private val TAG = "OkHttpHelper"

    /**
     * 使用正规认证的ssl证书
     *
     * @return
     */
    fun createHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(HandleErrorInterceptor())
    }

    fun test() {

    }


    /**
     * 使用自签名的ssl证书
     *
     * @param sslContext
     * @return
     */
    fun createHttpsClientBuilder(sslContext: SSLContext?): OkHttpClient.Builder {
        if (sslContext == null) {
            return createHttpClientBuilder()
        }
        val hostnameVerifier: HostnameVerifier =
            HostnameVerifier { hostname, session -> ("us.anlian.co" == hostname) }
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(HandleErrorInterceptor())
            .connectionSpecs(listOf(getConnectionSpec()) as List<ConnectionSpec>)
        return builder
            .sslSocketFactory(sslContext.socketFactory, getDefaultTrustManager()!!)
            .hostnameVerifier(hostnameVerifier)
    }

    private fun getConnectionSpec(): ConnectionSpec? {
        return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
                TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256,
                TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                TLS_RSA_WITH_AES_128_GCM_SHA256,
                TLS_RSA_WITH_AES_256_GCM_SHA384,
                TLS_RSA_WITH_AES_128_CBC_SHA,
                TLS_RSA_WITH_AES_256_CBC_SHA
            )
            .build()
    }

    private fun getDefaultTrustManager(): X509TrustManager? {
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm()
            )
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers.get(0) !is X509TrustManager) {
                throw IllegalStateException(
                    "Unexpected default trust managers:"
                            + Arrays.toString(trustManagers)
                )
            }
            return trustManagers[0] as X509TrustManager
        } catch (e: GeneralSecurityException) {
            throw AssertionError()
        }
    }
}