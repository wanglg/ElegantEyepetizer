package com.leowong.project.eyepetizer.ui.fragments

import android.os.Bundle
import com.agile.android.leo.exception.ApiException
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.BaseFragment
import com.leowong.project.eyepetizer.mvp.contract.HomeContract
import com.leowong.project.eyepetizer.mvp.model.HomeModel
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.mvp.presenter.HomePresenter
import com.leowong.project.eyepetizer.showToast
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomePresenter>(), HomeContract.View, OnRefreshListener {


    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initPresenter() {
        mPresenter = HomePresenter(HomeModel(), this)
    }

    override fun configViews() {
        multipleStatusView = homeMultipleStatusView;
        multipleStatusView?.setOnRetryClickListener({
            requestData()
        })
        mRefreshLayout.setOnRefreshListener(this)
    }

    override fun initData(savedInstanceState: Bundle?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestData() {
        mPresenter?.requestHomeData(1)
    }

    override fun showNoNetWork() {
        showToast(resources.getString(R.string.no_net_msg))
    }
    override fun onRefresh(refreshLayout: RefreshLayout?) {
    }

    override fun setHomeData(homeBean: HomeBean) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resultError(exception: ApiException) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}