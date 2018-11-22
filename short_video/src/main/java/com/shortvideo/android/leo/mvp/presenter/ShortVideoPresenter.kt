package com.shortvideo.android.leo.mvp.presenter

import com.agile.android.leo.mvp.BasePresenter
import com.shortvideo.android.leo.mvp.contract.SmallVideoContract

class ShortVideoPresenter(model: SmallVideoContract.Model, rootView: SmallVideoContract.View) :
        BasePresenter<SmallVideoContract.Model, SmallVideoContract.View>(model, rootView) {
}