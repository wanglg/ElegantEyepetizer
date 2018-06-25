package com.agile.android.leo.mvp

import com.agile.android.leo.exception.ApiException

/**
 * view接口基类
 * Created by caowt on 2017/12/27 0027.
 */

interface IView {
    fun resultError(exception: ApiException)

    fun showLoading()

    fun dismissLoading()
}
