package com.leowong.project.eyepetizer.mvp.contract

import com.agile.android.leo.mvp.IModel
import com.agile.android.leo.mvp.IView
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import io.reactivex.Observable

interface HomeContract {
    interface View : IView {
        /**
         * 设置第一次请求的数据
         */
        fun setHomeData(homeBean: HomeBean)

        /**
         * 设置加载更多的数据
         */
        fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>)

        fun showNoNetWork()
        /**
         * 加载更多失败
         */
        fun loadMoreFailed()


    }

    interface Model : IModel {
        fun requestHomeData(num: Int): Observable<HomeBean>

        fun loadMoreData(url: String): Observable<HomeBean>

    }
}