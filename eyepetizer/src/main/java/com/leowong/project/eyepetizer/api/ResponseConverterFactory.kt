package com.leowong.project.eyepetizer.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.android.leo.base.utils.GsonUtils
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

//自定义一个响应变换工厂

class ResponseConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {


    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson, adapter)
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonResponseBodyConverter(gson, adapter)
    }

    companion object {
        @JvmOverloads
        fun create(gson: Gson = GsonBuilder()//建造者模式设置不同的配置
                .serializeNulls()//序列化为null对象
                .disableHtmlEscaping()//防止对网址乱码 忽略对特殊字符的转换
                .registerTypeAdapter(String::class.java, GsonUtils.StringConverter())//对为null的字段进行转换
                .create()): ResponseConverterFactory {
            return ResponseConverterFactory(gson)
        }
    }
}