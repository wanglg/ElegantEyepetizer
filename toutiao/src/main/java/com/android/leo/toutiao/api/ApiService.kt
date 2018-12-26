package com.android.leo.toutiao.api

import com.android.leo.toutiao.api.ApiConstant.GET_ARTICLE_LIST
import com.android.leo.toutiao.mvp.model.entity.VideoModel
import com.android.leo.toutiao.mvp.model.response.NewsResponse
import com.android.leo.toutiao.mvp.model.response.ResultResponse
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
    fun getNewsList(@Query("category") category: String, @Query("max_behot_time") max_behot_time: Long): Observable<NewsResponse>

    /**
     * 获取视频页的html代码
     */
    @GET
    fun getVideoHtml(@Url url: String): Observable<String>

    /**
     * 获取视频数据json
     * @param url
     * @return
     */
    @GET
    fun getVideoData(@Url url: String): Observable<ResultResponse<VideoModel>>
}