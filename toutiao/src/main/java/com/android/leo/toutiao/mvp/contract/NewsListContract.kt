package com.android.leo.toutiao.mvp.contract

import com.agile.android.leo.mvp.IModel
import com.agile.android.leo.mvp.IView
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.mvp.model.entity.NewsData
import com.android.leo.toutiao.mvp.model.response.NewsResponse
import io.reactivex.Observable

interface NewsListContract {
    interface View : IView {
        fun onGetNewsListSuccess(newList: List<News>, tipInfo: String)
    }

    interface Model : IModel {
        fun getNewsLst(channelCode: String, lastTime: Long, currentTIme: Long): Observable<NewsResponse>
    }
}