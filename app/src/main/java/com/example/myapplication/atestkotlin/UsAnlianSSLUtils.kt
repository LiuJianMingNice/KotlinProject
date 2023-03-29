package com.example.myapplication.atestkotlin

import android.content.Context
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.security.*
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * UsAnlianSSLUtils
 * @author liujianming
 * @date 2022-01-05
 */
class UsAnlianSSLUtils {
    private val TAG = "UsAnlianSSLUtils"

    private val CA_CER_FILE = "ca.cer"

    private var sSSLContext: SSLContext? = null

    /**
     * 使用自签名证书
     *
     * @param context
     * @return
     */
    fun getSelfSignSslContext(context: Context): SSLContext? {
        if (sSSLContext == null) {
            try {
                val cf = CertificateFactory.getInstance("X.509")
                val caIn: InputStream =
                    BufferedInputStream(context.resources.assets.open(CA_CER_FILE))
                val ca: Certificate
                ca = try {
                    cf.generateCertificate(caIn)
                } finally {
                    caIn.close()
                }
                sSSLContext = SSLContext.getInstance("TLSv1.2")
                sSSLContext?.init(null, arrayOf<X509TrustManager>(object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                        //校验客户端证书
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                        //校验服务器证书
                        for (cert in chain) {
                            cert.checkValidity()
                            try {
                                cert.verify(ca.publicKey)
                            } catch (e: NoSuchAlgorithmException) {
                                e.printStackTrace()
                            } catch (e: InvalidKeyException) {
                                e.printStackTrace()
                            } catch (e: NoSuchProviderException) {
                                e.printStackTrace()
                            } catch (e: SignatureException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }
                }), SecureRandom())
            } catch (e: CertificateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            }
        }
        return sSSLContext
    }


    /**
     * 使用正规CA授权的证书
     *
     * @param context
     * @return
     */
    open fun getCaSslContext(context: Context?): SSLContext? {
        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, SecureRandom())
        } catch (e: Exception) {
//            RSLog.e(TAG, e, "#getCaSSLContext : %s", e.getMessage());
        }
        return sslContext
    }
}