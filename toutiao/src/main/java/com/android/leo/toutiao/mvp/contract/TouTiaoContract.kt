package com.android.leo.toutiao.mvp.contract

import com.agile.android.leo.mvp.IModel
import com.agile.android.leo.mvp.IView
import com.android.leo.toutiao.mvp.model.entity.Channel

interface TouTiaoContract {
    interface View : IView {

    }

    interface Model : IModel {
        fun getSelectChannels(): ArrayList<Channel>
    }
}