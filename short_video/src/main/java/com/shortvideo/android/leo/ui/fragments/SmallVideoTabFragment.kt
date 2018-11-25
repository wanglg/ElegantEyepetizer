package com.shortvideo.android.leo.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import com.agile.android.leo.exception.ApiException
import com.android.leo.base.GlobalConstant
import com.android.leo.base.ui.fragments.BaseFragment
import com.leowang.shortvideo.R
import com.sankuai.waimai.router.annotation.RouterService
import com.shortvideo.android.leo.mvp.contract.SmallVideoContract
import com.shortvideo.android.leo.mvp.model.SmallVideoModel
import com.shortvideo.android.leo.mvp.model.entity.VideoBean
import com.shortvideo.android.leo.mvp.presenter.ShortVideoPresenter
import com.shortvideo.android.leo.ui.adapters.ShortVideoTabAdapter
import kotlinx.android.synthetic.main.fragment_small_video_tab.*

@RouterService(interfaces = arrayOf(Fragment::class), key = arrayOf(GlobalConstant.Fragment.LITTLE_VIDEO))
class SmallVideoTabFragment : BaseFragment<ShortVideoPresenter>(), SmallVideoContract.View {


    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val shortVideoTabAdapter by lazy {
        ShortVideoTabAdapter(ArrayList())
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_small_video_tab;
    }

    override fun initPresenter() {
        mPresenter = ShortVideoPresenter(SmallVideoModel(), this)
    }

    override fun configViews() {
        mRefreshLayout.setOnRefreshListener({

        })
        recycler_pager_view.layoutManager = linearLayoutManager
        recycler_pager_view.adapter = shortVideoTabAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun requestData() {
        mPresenter?.requestVideoData()
    }

    override fun setVideoData(data: ArrayList<VideoBean>) {
        shortVideoTabAdapter.replaceData(data)
    }

    override fun resultError(exception: ApiException) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}