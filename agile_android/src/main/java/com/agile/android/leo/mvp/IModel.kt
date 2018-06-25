package com.agile.android.leo.mvp

/**
 * User: wanglg
 * Date: 2018-05-02
 * Time: 12:11
 * FIXME
 */
interface IModel {
    /**
     * 在框架中 [BasePresenter.onDestroy] 时会默认调用 [IModel.onDestroy]
     */
    fun onDestroy()
}