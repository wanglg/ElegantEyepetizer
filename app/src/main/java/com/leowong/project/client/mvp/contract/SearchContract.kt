package com.hazz.kotlinmvp.mvp.contract

import com.agile.android.leo.mvp.IModel
import com.agile.android.leo.mvp.IView
import com.leowong.project.client.mvp.models.beans.HomeBean

/**
 * Created by xuhao on 2017/11/30.
 * desc: 搜索契约类
 */
interface SearchContract {

    interface View : IView {
        /**
         * 设置热门关键词数据
         */
        fun setHotWordData(string: ArrayList<String>)

        /**
         * 设置搜索关键词返回的结果
         */
        fun setSearchResult(issue: HomeBean.Issue)

        /**
         * 关闭软件盘
         */
        fun closeSoftKeyboard()

        /**
         * 设置空 View
         */
        fun setEmptyView()


        fun showError(errorMsg: String, errorCode: Int)
    }

    interface Model : IModel {
        /**
         * 获取热门关键字的数据
         */
        fun requestHotWordData()

        /**
         * 查询搜索
         */
        fun querySearchData(words: String)

        /**
         * 加载更多
         */
        fun loadMoreData()
    }
}