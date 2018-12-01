package com.android.leo.toutiao.mvp.model

import com.agile.android.leo.mvp.BaseModel
import com.android.leo.toutiao.api.ApiService
import com.android.leo.toutiao.api.RepositoryManager
import com.android.leo.toutiao.mvp.contract.NewsListContract
import com.android.leo.toutiao.mvp.model.response.NewsResponse
import io.reactivex.Observable

class NewsListModel : BaseModel(), NewsListContract.Model {
    override fun getNewsLst(channelCode: String, lastTime: Long, currentTIme: Long): Observable<NewsResponse> {
        return RepositoryManager.obtainRetrofitService(ApiService::class.java).getNewsList(channelCode, lastTime, currentTIme)
    }
}