package com.android.leo.toutiao.ui.fragment

import android.os.Bundle
import com.agile.android.leo.exception.ApiException
import com.android.leo.base.ui.fragments.BaseFragment
import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.contract.NewsListContract
import com.android.leo.toutiao.mvp.model.NewsListModel
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.mvp.presenter.NewsListPresenter
import kotlinx.android.synthetic.main.fragment_news_list.*

class NewsListFragment : BaseFragment<NewsListPresenter>(), NewsListContract.View {
    override fun onGetNewsListSuccess(newList: List<News>, tipInfo: String) {
    }


    override fun resultError(exception: ApiException) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_news_list
    }

    override fun initPresenter() {
        mPresenter = NewsListPresenter(NewsListModel(), this)
    }

    override fun configViews() {
        text.setText("sada")
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun requestData() {
        mPresenter?.requestNewsList("video")
    }
}