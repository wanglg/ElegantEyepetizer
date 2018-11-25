package com.shortvideo.android.leo.mvp.contract

import com.agile.android.leo.mvp.IModel
import com.agile.android.leo.mvp.IView
import com.shortvideo.android.leo.mvp.model.entity.VideoBean
import io.reactivex.Observable

interface SmallVideoContract {
    interface View : IView {
        fun setVideoData(data: ArrayList<VideoBean>)
    }

    interface Model : IModel {
        fun getVideoData(): Observable<ArrayList<VideoBean>>
    }
}