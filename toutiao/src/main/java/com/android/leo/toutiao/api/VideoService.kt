package com.android.leo.toutiao.api

import com.android.leo.toutiao.mvp.model.entity.VideoModel
import com.android.leo.toutiao.mvp.model.response.ResultResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface VideoService {


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