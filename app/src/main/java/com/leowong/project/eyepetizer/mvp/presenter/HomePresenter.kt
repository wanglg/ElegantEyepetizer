package com.leowong.project.eyepetizer.mvp.presenter

import com.agile.android.leo.exception.ApiException
import com.agile.android.leo.mvp.BasePresenter
import com.leowong.project.eyepetizer.api.ApiSubscriber
import com.leowong.project.eyepetizer.mvp.contract.HomeContract
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.utils.rxjava.SchedulersUtil
import io.reactivex.disposables.Disposable

class HomePresenter(model: HomeContract.Model, rootView: HomeContract.View) :
        BasePresenter<HomeContract.Model, HomeContract.View>(model, rootView) {
    fun requestHomeData(num: Int) {
        mRootView?.showLoading()
        mModel?.requestHomeData(num)?.compose(SchedulersUtil.applyApiSchedulers())
                ?.subscribe(object : ApiSubscriber<HomeBean>() {
                    override fun onFailure(t: ApiException) {
                    }

                    override fun onSubscribe(d: Disposable) {
                        addDispose(d)
                    }

                    override fun onNext(t: HomeBean) {
                    }

                    override fun onNetWorkError() {
                        mRootView?.showNoNetWork()
                    }

                })
    }
}