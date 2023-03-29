package com.example.myapplication.atest;

import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author liuaibin
 */
public class ServiceGenerator {

    private static final String TAG = "ServiceGenerator";
    private static volatile Retrofit.Builder sBuilder;

    private static Retrofit.Builder getRetrofitBuilder() {
        if (sBuilder == null) {
            synchronized (Retrofit.Builder.class) {
                if (sBuilder == null) {
                    sBuilder = createBuilder();
                }
            }
        }
        return sBuilder;
    }

    /**
     * 创建"基本配置"的 Retrofit.Builder, 只能被 {@link #getRetrofitBuilder} 使用
     */
    @NonNull
    private static Retrofit.Builder createBuilder() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    /**
     * 无需鉴权认证的接口
     */
    public static <S> S createService(final @NonNull OkHttpClient.Builder clientBuilder,
                                      final @NonNull Class<S> serviceClass,
                                      String baseUrl) {

        OkHttpClient.Builder httpClientBuilder = clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("X-RS-Request-ID", UUID.randomUUID().toString())
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return getRetrofitBuilder()
                .baseUrl(baseUrl)
                .client(httpClientBuilder.build())
                .build()
                .create(serviceClass);
    }

    /**
     * 认证(Authentication)相关的 createService(...) 方法 ↓
     * <p>
     * Basic Authentication on Android
     * {a http://blog.csdn.net/kiwi_coder/article/details/28677651}
     */
    public static <S> S createService(final @NonNull OkHttpClient.Builder clientBuilder,
                                      Class<S> serviceClass,
                                      String baseUrl,
                                      final String username,
                                      final String password) {
        if (username != null && password != null) {
            final String basic = Credentials.basic(username, password);

            OkHttpClient.Builder httpClientBuilder = clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", basic)
                            .header("X-RS-Request-ID", UUID.randomUUID().toString())
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            return getRetrofitBuilder()
                    .baseUrl(baseUrl)
                    .client(httpClientBuilder.build())
                    .build()
                    .create(serviceClass);
        } else {
            throw new IllegalArgumentException("'username' and 'password' must not null!");
        }
    }

    /**
     * Token Authentication on Android
     * {a http://blog.csdn.net/kiwi_coder/article/details/28677651}
     */
    public static <S> S createService(final @NonNull OkHttpClient.Builder clientBuilder,
                                      Class<S> serviceClass,
                                      String baseUrl,
                                      final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            OkHttpClient.Builder httpClientBuilder = clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", authToken)
                            .header("X-RS-Request-ID", UUID.randomUUID().toString())
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            return getRetrofitBuilder()
                    .baseUrl(baseUrl)
                    .client(httpClientBuilder.build())
                    .build()
                    .create(serviceClass);
        } else {
            throw new IllegalArgumentException("'authToken' must not empty!");
        }
    }

    /**
     * OAuth on Android
     * {a http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html}
     * {a https://futurestud.io/blog/oauth-2-on-android-with-retrofit}
     */
    public static <S> S createService(final @NonNull OkHttpClient.Builder clientBuilder,
                                      Class<S> serviceClass,
                                      String baseUrl,
                                      final AccessToken token) {
        if (token != null) {
            OkHttpClient.Builder httpClientBuilder = clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization",
                                    token.getTokenType() + " " + token.getAccessToken())
                            .header("X-RS-Request-ID", UUID.randomUUID().toString())
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            return getRetrofitBuilder()
                    .baseUrl(baseUrl)
                    .client(httpClientBuilder.build())
                    .build()
                    .create(serviceClass);
        } else {
            throw new IllegalArgumentException("'token' must not empty!");
        }
    }

    public static class AccessToken {
        private String mAccessToken;
        private String mTokenType;

        public AccessToken() {
        }

        public String getAccessToken() {
            return mAccessToken;
        }

        public void setAccessToken(String accessToken) {
            mAccessToken = accessToken;
        }

        public String getTokenType() {
            // OAuth requires uppercase Authorization HTTP header value for token type
            if (!Character.isUpperCase(mTokenType.charAt(0))) {
                mTokenType = Character.toString(mTokenType.charAt(0))
                        .toUpperCase() + mTokenType.substring(1);
            }

            return mTokenType;
        }

        public void setTokenType(String tokenType) {
            mTokenType = tokenType;
        }
    }
}
