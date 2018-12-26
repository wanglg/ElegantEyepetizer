package com.android.leo.toutiao.mvp.contract

import com.agile.android.leo.exception.ApiException
import com.agile.android.leo.mvp.IModel
import com.agile.android.leo.mvp.IView
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.mvp.model.entity.NewsData
import com.android.leo.toutiao.mvp.model.response.NewsResponse
import io.reactivex.Observable

interface NewsListContract {
    interface View : IView {
        fun onGetNewsListSuccess(newList: ArrayList<News>, tipInfo: String)
        fun addToEndListSuccess(newList: ArrayList<News>)
        fun addToEndListFailed(e: ApiException)
    }

    interface Model : IModel {
        fun getNewsLst(channelCode: String, max_behot_time: Long): Observable<NewsResponse>
    }
}