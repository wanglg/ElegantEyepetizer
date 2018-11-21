package com.leowong.project.eyepetizer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.agile.android.leo.exception.ApiException
import com.android.leo.base.Constant
import com.chad.library.adapter.base.BaseQuickAdapter
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.AppBaseFragment
import com.leowong.project.eyepetizer.mvp.contract.HomeContract
import com.leowong.project.eyepetizer.mvp.model.HomeModel
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.mvp.presenter.HomePresenter
import com.leowong.project.eyepetizer.showToast
import com.leowong.project.eyepetizer.ui.activities.VideoDetailActivityApp
import com.leowong.project.eyepetizer.ui.adapters.HomeAdapter
import com.leowong.project.eyepetizer.ui.adapters.entity.HomeMultipleEntity
import com.sankuai.waimai.router.annotation.RouterService
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_home.*

@RouterService(interfaces = arrayOf(Fragment::class), key = arrayOf(Constant.Fragment.EYEPETIZER))
class HomeFragment : AppBaseFragment<HomePresenter>(), HomeContract.View, OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {


    private var mTitle: String? = null
    protected var homeAdapter: HomeAdapter? = null

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
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
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
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
        val item = adapter.getItem(position) as HomeMultipleEntity;
        val intent = Intent(activity, VideoDetailActivityApp::class.java)
        intent.putExtra(VideoDetailActivityApp.BUNDLE_VIDEO_DATA, item.homeBean)
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