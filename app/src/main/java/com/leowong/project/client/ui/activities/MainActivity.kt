package com.leowong.project.client.ui.activities

import com.agile.android.leo.mvp.IPresenter
import com.leowong.project.client.R
import com.leowong.project.client.base.BaseActivity

class MainActivity : BaseActivity<IPresenter>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initPresenter() {
    }

    override fun configViews() {
    }

    override fun initData() {
    }
}
