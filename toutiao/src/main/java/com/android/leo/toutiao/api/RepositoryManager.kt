package com.android.leo.toutiao.api

import android.content.Context
import com.agile.android.leo.integration.IRepositoryManager
import com.android.leo.base.BaseApplication
import com.android.leo.base.BuildConfig
import com.leo.android.api.logger.HttpLogger
import com.leo.android.api.logger.HttpLoggingInterceptor
import com.leo.android.api.retrofit.ApiConfig
import com.leo.android.api.retrofit.RetrofitManager
import okhttp3.Interceptor

object RepositoryManager : IRepositoryManager {
    override fun <T : Any?> obtainRetrofitService(service: Class<T>): T {
        val apiConfig = ApiConfig.Builder().setBaseUrl(ApiConstant.BASE_SERVER_URL).setTimeOut(30)
                .addInterceptor(addHeaderInterceptor())
//                .addInterceptor(addQueryParameterInterceptor())
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
//    private fun addQueryParameterInterceptor(): Interceptor {
//        return Interceptor { chain ->
//            val originalRequest = chain.request()
//            val request: Request
//            val modifiedUrl = originalRequest.url().newBuilder()
//                    // Provide your custom parameter here
//                    .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
////                    .addQueryParameter("deviceModel", AppUtils.getMobileModel())
//                    .build()
//            request = originalRequest.newBuilder().url(modifiedUrl).build()
//            chain.proceed(request)
//        }
//    }


    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.0.0.13547")
            builder.addHeader("Cache-Control", "max-age=0")
            builder.addHeader("Upgrade-Insecure-Requests", "1")
            builder.addHeader("X-Requested-With", "XMLHttpRequest")
            builder.addHeader("Cookie", "uuid=\"w:f2e0e469165542f8a3960f67cb354026\"; __tasessionId=4p6q77g6q1479458262778; csrftoken=7de2dd812d513441f85cf8272f015ce5; tt_webid=36385357187")
            chain.proceed(builder.build())
        }
    }

}