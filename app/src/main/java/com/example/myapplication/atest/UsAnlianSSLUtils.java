package com.example.myapplication.atest;

import android.content.Context;



import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.Nullable;

/**
 * @author liuaibin
 */
public class UsAnlianSSLUtils {
    private static final String TAG = "UsAnlianSSLUtils";

    private final static String CA_CER_FILE = "ca.cer";

    private static SSLContext sSSLContext = null;

    /**
     * 使用自签名证书
     *
     * @param context
     * @return
     */
    @Nullable
    public static SSLContext getSelfSignSslContext(Context context) {
        if (sSSLContext == null) {
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream caIn = new BufferedInputStream(context.getResources().getAssets().open(CA_CER_FILE));
                final Certificate ca;
                try {
                    ca = cf.generateCertificate(caIn);
                } finally {
                    caIn.close();
                }

                sSSLContext = SSLContext.getInstance("TLSv1.2");
                sSSLContext.init(null, new X509TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        //校验客户端证书
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        //校验服务器证书
                        for (X509Certificate cert : chain) {
                            cert.checkValidity();
                            try {
                                cert.verify(ca.getPublicKey());
                            } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }}, new SecureRandom());
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }

        return sSSLContext;
    }



    /**
     * 使用正规CA授权的证书
     *
     * @param context
     * @return
     */
    @Nullable
    public static SSLContext getCaSslContext(Context context) {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, new SecureRandom());
        } catch (Exception e) {
//            RSLog.e(TAG, e, "#getCaSSLContext : %s", e.getMessage());
        }

        return sslContext;
    }
}
