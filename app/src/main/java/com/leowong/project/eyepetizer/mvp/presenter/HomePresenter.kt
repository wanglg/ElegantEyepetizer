package com.leowong.project.eyepetizer.mvp.presenter

import com.agile.android.leo.mvp.BasePresenter
import com.leowong.project.eyepetizer.mvp.contract.HomeContract
import com.leowong.project.eyepetizer.utils.LogUtils

class HomePresenter(model: HomeContract.Model, rootView: HomeContract.View) :
        BasePresenter<HomeContract.Model, HomeContract.View>(model, rootView) {
    fun requestHomeData(num: Int) {
        mRootView?.showLoading()
        mModel?.requestHomeData(num)
                ?.subscribe({
                    LogUtils.d("sasa")
                })
    }
}