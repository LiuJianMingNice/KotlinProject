package com.example.myapplication.atestkotlin

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * AbstractResponseBodyInterceptor
 * @author liujianming
 * @date 2022-01-05
 */
abstract class AbstractResponseBodyInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val url = request.url.toString()
        val response: Response = chain.proceed(request)
        val responseBody = response.body
        if (responseBody != null) {
            val contentLength = responseBody.contentLength()
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            var buffer = source.buffer
            if ("gzip" == response.headers["Content-Encoding"]) {
                val gzippedResponseBody = GzipSource(buffer.clone())
                buffer = Buffer()
                buffer.writeAll(gzippedResponseBody)
            }
            val contentType = responseBody.contentType()
            val charset: Charset?
            charset =
                if (contentType == null || contentType.charset(StandardCharsets.UTF_8) == null) {
                    StandardCharsets.UTF_8
                } else {
                    contentType.charset(StandardCharsets.UTF_8)
                }
            if (charset != null && contentLength != 0L) {
                return intercept(response, url, buffer.clone().readString(charset))
            }
        }
        return response
    }

    @Throws(IOException::class)
    abstract fun intercept(response: Response, url: String?, body: String?): Response
}