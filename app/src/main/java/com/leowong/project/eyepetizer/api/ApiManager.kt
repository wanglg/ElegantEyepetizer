package  com.leowong.project.eyepetizer.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.leowong.project.eyepetizer.BuildConfig
import com.leowong.project.eyepetizer.utils.GsonUtils
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSink
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * User: wanglg
 * Date: 2018-05-14
 * Time: 11:38
 * FIXME
 */
class ApiManager {
    val gson: Gson? get() = mGson

    //初始化apimanager
    companion object {
        private var apiManager: ApiManager? = null
        private var mGson: Gson? = null
        private val TIMEOUT: Long = 30

        //建造者模式设置不同的配置
        //序列化为null对象
        //防止对网址乱码 忽略对特殊字符的转换
        //对为null的字段进行转换
        val instance: ApiManager
            @Synchronized get() {
                if (apiManager == null) {
                    apiManager = ApiManager()
                }
                if (mGson == null) {
                    mGson = GsonBuilder()
                            .serializeNulls()
                            .disableHtmlEscaping()
                            .registerTypeAdapter(String::class.java, GsonUtils.StringConverter())
                            .create()
                }
                return apiManager as ApiManager
            }

        private val httpClient = OkHttpClient.Builder()
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
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()

        private var sRetrofit = Retrofit.Builder()
                .baseUrl(UrlConstants.BASE_URL)
                .addConverterFactory(ResponseConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()

        private var apiManagerService = sRetrofit.create(ApiManagerService::class.java)
    }

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
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()

    }


}