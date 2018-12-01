package com.android.leo.toutiao.mvp.presenter

import com.agile.android.leo.mvp.BasePresenter
import com.android.leo.toutiao.mvp.contract.TouTiaoContract
import com.android.leo.toutiao.mvp.model.entity.Channel
import io.reactivex.Observable

class TouTiaoPresenter(model: TouTiaoContract.Model, rootView: TouTiaoContract.View) : BasePresenter<TouTiaoContract.Model, TouTiaoContract.View>(model, rootView) {

    fun requestSelectChannel(): Observable<List<Channel>> {
        return Observable.just(mModel?.getSelectChannels())
    }
}