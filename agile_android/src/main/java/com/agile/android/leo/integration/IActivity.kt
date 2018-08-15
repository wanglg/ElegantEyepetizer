package com.agile.android.leo.integration

import android.os.Bundle

/**
 * User: wanglg
 * Date: 2018-05-10
 * Time: 20:14
 * FIXME
 */
interface IActivity {
    fun getLayoutId(): Int
    fun initPresenter()
    fun configViews()
    fun initData(savedInstanceState: Bundle?)
    fun requestData()
}