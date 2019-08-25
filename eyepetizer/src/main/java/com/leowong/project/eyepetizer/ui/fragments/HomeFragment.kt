package com.leowong.project.eyepetizer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.RelativeLayout
import com.agile.android.leo.exception.ApiException
import com.android.leo.base.GlobalConstant
import com.android.leo.base.showToast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.leo.android.log.core.LogUtils
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.AppBaseFragment
import com.leowong.project.eyepetizer.mvp.contract.HomeContract
import com.leowong.project.eyepetizer.mvp.model.HomeModel
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.mvp.presenter.HomePresenter
import com.leowong.project.eyepetizer.ui.activities.VideoDetailActivity
import com.leowong.project.eyepetizer.ui.adapters.HomeAdapter
import com.leowong.project.eyepetizer.ui.adapters.entity.HomeMultipleEntity
import com.sankuai.waimai.router.annotation.RouterService
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_home.*

@RouterService(interfaces = arrayOf(Fragment::class), key = arrayOf(GlobalConstant.Fragment.EYEPETIZER))
class HomeFragment : AppBaseFragment<HomePresenter>(), HomeContract.View, OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {


    protected var homeAdapter: HomeAdapter? = null

    private var linearLayoutManager: LinearLayoutManager? = null

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
        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRefreshLayout.setOnRefreshListener(this)
        val lp = status_bar_view.layoutParams as RelativeLayout.LayoutParams
        lp.height = ImmersionBar.getStatusBarHeight(activity!!)
        status_bar_view.layoutParams = lp
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun requestData() {
        mPresenter?.requestHomeData(1)
    }

    override fun showNoNetWork() {
        showToast(resources.getString(R.string.no_net_msg))
        if (homeAdapter == null || homeAdapter?.itemCount == 0) {
            multipleStatusView?.showNoNetwork()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        homeAdapter?.setEnableLoadMore(true);
        requestData()
    }


    override fun setHomeData(homeBean: HomeBean) {
        multipleStatusView?.showContent()
        homeAdapter = HomeAdapter(ArrayList())
        homeAdapter?.addItemData(homeBean.issueList[0].itemList)
        homeAdapter?.setOnLoadMoreListener(this, mRecyclerView)
        homeAdapter?.onItemClickListener = this
        mRecyclerView.adapter = homeAdapter
    }

    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {
        if (itemList.size == 0) {
            homeAdapter?.loadMoreEnd()
        } else {
            homeAdapter?.loadMoreComplete()
            homeAdapter?.addItemData(itemList)
        }
    }

    override fun resultError(exception: ApiException) {
    }

    override fun loadMoreFailed() {
        homeAdapter?.loadMoreFail()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        LogUtils.d("wang","onItemClick")
        val item = adapter.getItem(position) as HomeMultipleEntity;
        val intent = Intent(activity, VideoDetailActivity::class.java)
        intent.putExtra(VideoDetailActivity.BUNDLE_VIDEO_DATA, item.homeBean)
        startActivity(intent)

    }

    override fun showLoading() {
        if (homeAdapter == null || homeAdapter?.itemCount == 0) {
            multipleStatusView?.showLoading()
        }
    }

    override fun onLoadMoreRequested() {
        mPresenter?.loadMore()
    }

    override fun dismissLoading() {
        mRefreshLayout.finishRefresh()
    }
}