package com.agile.android.leo.mvp

import android.app.Activity

/**
 * User: wanglg
 * Date: 2018-05-02
 * Time: 12:10
 * FIXME
 */
interface IPresenter {
    /**
     * 做一些初始化操作
     */
    fun onStart()

    /**
     * 在框架中 [Activity.onDestroy] 时会默认调用 [IPresenter.onDestroy]
     */
    fun onDestroy()
}