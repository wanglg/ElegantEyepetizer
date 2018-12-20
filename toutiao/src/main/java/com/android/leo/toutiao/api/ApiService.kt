package com.android.leo.toutiao.api

import com.android.leo.toutiao.api.ApiConstant.GET_ARTICLE_LIST
import com.android.leo.toutiao.mvp.model.response.NewsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {


    /**
     * 获取新闻列表
     *
     * @param category 频道
     * @return
     */
    @GET(GET_ARTICLE_LIST)
    fun getNewsList(@Query("category") category: String, @Query("min_behot_time") lastTime: Long,
                    @Query("last_refresh_sub_entrance_interval") currentTime: Long): Observable<NewsResponse>

    /**
     * 获取视频页的html代码
     */
    @GET
    fun getVideoHtml(@Url url: String): Observable<String>
}