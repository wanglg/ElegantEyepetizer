package com.leowong.project.eyepetizer.api

import android.preference.Preference
import com.leowong.project.eyepetizer.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSink
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by xuhao on 2017/11/16.
 *
 */

object RetrofitManager {

    val service: ApiManagerService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(ApiManagerService::class.java)
    }
    private val TIMEOUT: Long = 30
    private var token: String = ""
//    private var token:String by Preference("token","")

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
                    .header("token", token)
                    .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * 设置缓存
     */
//    private fun addCacheInterceptor(): Interceptor {
//        return Interceptor { chain ->
//            var request = chain.request()
//            if (!NetworkUtil.isNetworkAvailable(MyApplication.context)) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build()
//            }
//            val response = chain.proceed(request)
//            if (NetworkUtil.isNetworkAvailable(MyApplication.context)) {
//                val maxAge = 0
//                // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
//                response.newBuilder()
//                        .header("Cache-Control", "public, max-age=" + maxAge)
//                        .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//                        .build()
//            } else {
//                // 无网络时，设置超时为4周  只对get有用,post没有缓冲
//                val maxStale = 60 * 60 * 24 * 28
//                response.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .removeHeader("nyn")
//                        .build()
//            }
//            response
//        }
//    }


    private fun getRetrofit(): Retrofit {
        // 获取retrofit的实例
        return Retrofit.Builder()
                .baseUrl(UrlConstants.BASE_URL)  //自己配置
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ResponseConverterFactory.create())
                .build()

    }

    private fun getRetrofit(baseUrl: String): Retrofit {
        // 获取retrofit的实例
        return Retrofit.Builder()
                .baseUrl(baseUrl)  //自己配置
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ResponseConverterFactory.create())
                .build()

    }

    private fun getOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
                // 添加通用的Header 面试官喜欢问
                .addInterceptor { chain ->
                    val request = chain.request()
                    val builder = request.newBuilder()

                    if (request.method() == "POST" && request.body() is FormBody) {

                        val body = request.body()
                        builder.post(object : RequestBody() {
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
                    chain.proceed(builder.build())
                }
                /*
            这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
            出了bug很难找出来问题，添加HttpLoggingInterceptor拦截器方便调试接口
             */
                .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }))
                .addInterceptor(addHeaderInterceptor())
                .addInterceptor(addQueryParameterInterceptor())
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()

    }

}
