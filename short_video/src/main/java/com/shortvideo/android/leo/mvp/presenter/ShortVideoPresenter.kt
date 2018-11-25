package com.shortvideo.android.leo.mvp.presenter

import com.agile.android.leo.exception.ApiException
import com.agile.android.leo.mvp.BasePresenter
import com.android.leo.base.utils.rxjava.SchedulersUtil
import com.leo.android.api.ApiSubscriber
import com.shortvideo.android.leo.mvp.contract.SmallVideoContract
import com.shortvideo.android.leo.mvp.model.entity.VideoBean
import io.reactivex.disposables.Disposable

class ShortVideoPresenter(model: SmallVideoContract.Model, rootView: SmallVideoContract.View) :
        BasePresenter<SmallVideoContract.Model, SmallVideoContract.View>(model, rootView) {

    fun requestVideoData() {
        mModel?.getVideoData()?.compose(SchedulersUtil.applyApiSchedulers())?.subscribe(object : ApiSubscriber<ArrayList<VideoBean>>() {
            override fun onFailure(t: ApiException) {
            }

            override fun onSubscribe(d: Disposable) {
                addDispose(d)
            }

            override fun onNext(t: ArrayList<VideoBean>) {
                mRootView?.setVideoData(t)
            }

        })
    }
}