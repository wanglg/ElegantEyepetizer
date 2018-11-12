package com.leo.android.api.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitManager {


    public fun getRetrofit(apiConfig: ApiConfig): Retrofit {
        // 获取retrofit的实例
        return Retrofit.Builder()
                .baseUrl(apiConfig.baseUrl)  //自己配置
                .client(if (apiConfig.okHttpClient == null) {
                    getOkHttpClient(apiConfig)
                } else {
                    apiConfig.okHttpClient!!
                })
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }


    private fun getOkHttpClient(apiConfig: ApiConfig): OkHttpClient {
        val build = OkHttpClient.Builder()
        for (interceptor in apiConfig.interceptors) {
            build.addInterceptor(interceptor)
        }
        apiConfig.netInterceptor?.let {
            build.addNetworkInterceptor(it)
        }
        build.connectTimeout(apiConfig.timeOut, TimeUnit.SECONDS)
                .readTimeout(apiConfig.timeOut, TimeUnit.SECONDS)
                .writeTimeout(apiConfig.timeOut, TimeUnit.SECONDS)
        return build.build()
    }

}
