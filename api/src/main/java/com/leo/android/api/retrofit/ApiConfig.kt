package com.leo.android.api.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient

class ApiConfig {
    var baseUrl: String = ""
    var timeOut: Long = 30
    var interceptors: ArrayList<Interceptor> = ArrayList()
    var netInterceptor: Interceptor? = null
    var okHttpClient: OkHttpClient? = null

    class Builder {
        var target: ApiConfig = ApiConfig()
        fun setBaseUrl(baseUrl: String): Builder {
            this.target.baseUrl = baseUrl
            return this
        }

        fun setTimeOut(timeOut: Long): Builder {
            this.target.timeOut = timeOut
            return this
        }

        fun setOkHttpClient(okHttpClient: OkHttpClient): Builder {
            this.target.okHttpClient = okHttpClient
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            this.target.interceptors.add(interceptor)
            return this
        }

        fun addNetInterceptor(interceptor: Interceptor): Builder {
            this.target.netInterceptor = interceptor
            return this
        }

        fun build(): ApiConfig {
            return target
        }

    }
}