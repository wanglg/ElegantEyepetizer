package com.shortvideo.android.leo.mvp.model

import com.agile.android.leo.mvp.BaseModel
import com.shortvideo.android.leo.mvp.contract.SmallVideoContract
import com.shortvideo.android.leo.mvp.model.entity.VideoBean
import io.reactivex.Observable

class SmallVideoModel : SmallVideoContract.Model, BaseModel() {
    override fun requestVideoData(): Observable<ArrayList<VideoBean>> {
        return Observable.just(null)
    }
}