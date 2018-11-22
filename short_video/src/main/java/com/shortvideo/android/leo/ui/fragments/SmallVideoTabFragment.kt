package com.shortvideo.android.leo.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import com.agile.android.leo.mvp.IPresenter
import com.android.leo.base.GlobalConstant
import com.android.leo.base.ui.fragments.BaseFragment
import com.leowang.shortvideo.R
import com.sankuai.waimai.router.annotation.RouterService

@RouterService(interfaces = arrayOf(Fragment::class), key = arrayOf(GlobalConstant.Fragment.LITTLE_VIDEO))
class SmallVideoTabFragment : BaseFragment<IPresenter>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_small_video_tab;
    }

    override fun initPresenter() {
    }

    override fun configViews() {
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun requestData() {
    }
}