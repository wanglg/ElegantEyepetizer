package com.example.wang.goclient.ui.activities

import com.agile.android.leo.mvp.IPresenter
import com.example.wang.goclient.R
import com.example.wang.goclient.base.BaseActivity

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
