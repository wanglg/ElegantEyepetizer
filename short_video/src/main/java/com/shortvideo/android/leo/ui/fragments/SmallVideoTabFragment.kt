package com.shortvideo.android.leo.ui.fragments

import android.os.Bundle
import com.agile.android.leo.mvp.IPresenter
import com.android.leo.base.ui.fragments.BaseFragment
import com.leowang.shortvideo.R

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