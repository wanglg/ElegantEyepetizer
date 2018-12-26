package com.android.leo.toutiao.mvp.model

import com.agile.android.leo.mvp.BaseModel
import com.android.leo.toutiao.api.ApiService
import com.android.leo.toutiao.api.RepositoryManager
import com.android.leo.toutiao.mvp.contract.NewsListContract
import com.android.leo.toutiao.mvp.model.response.NewsResponse
import io.reactivex.Observable

class NewsListModel : BaseModel(), NewsListContract.Model {
    override fun getNewsLst(channelCode: String, max_behot_time: Long): Observable<NewsResponse> {
        return RepositoryManager.obtainRetrofitService(ApiService::class.java).getNewsList(channelCode, max_behot_time)
    }
}