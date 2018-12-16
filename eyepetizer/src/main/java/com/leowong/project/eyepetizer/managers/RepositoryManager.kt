package com.leowong.project.eyepetizer.managers

import android.content.Context
import com.agile.android.leo.integration.IRepositoryManager
import com.android.leo.base.BaseApplication
import com.leo.android.api.logger.HttpLogger
import com.leo.android.api.logger.HttpLoggingInterceptor
import com.leo.android.api.retrofit.ApiConfig
import com.leo.android.api.retrofit.RetrofitManager
import com.leowong.project.eyepetizer.BuildConfig
import com.leowong.project.eyepetizer.api.UrlConstants
import okhttp3.*
import okio.BufferedSink
import java.io.IOException

object RepositoryManager : IRepositoryManager {
    override fun <T : Any?> obtainRetrofitService(service: Class<T>): T {
        val apiConfig = ApiConfig.Builder().setBaseUrl(UrlConstants.BASE_URL).setTimeOut(30)
                .addInterceptor(addHeaderInterceptor())
                .addInterceptor(addQueryParameterInterceptor())
                .addNetInterceptor(HttpLoggingInterceptor(HttpLogger()).setLevel(if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }))

        return RetrofitManager.getRetrofit(apiConfig.build()).create(service)
    }

    override fun getContext(): Context {
        return BaseApplication.context
    }

    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                    // Provide your custom parameter here
                    .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
//                    .addQueryParameter("deviceModel", AppUtils.getMobileModel())
                    .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                    // Provide your custom header here
                    .header("token", "")
                    .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            if (request.method() == "POST" && request.body() is FormBody) {
                val body = request.body()
                requestBuilder.post(object : RequestBody() {
                    override fun contentType(): MediaType? {
                        return MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8")
                    }

                    @Throws(IOException::class)
                    override fun writeTo(sink: BufferedSink) {
                        body!!.writeTo(sink)
                    }

                    @Throws(IOException::class)
                    override fun contentLength(): Long {
                        return body!!.contentLength()
                    }
                })
            }
            chain.proceed(request)
        }
    }

}