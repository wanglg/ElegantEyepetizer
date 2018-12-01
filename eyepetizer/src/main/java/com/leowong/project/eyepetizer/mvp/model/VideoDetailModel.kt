package com.leowong.project.eyepetizer.mvp.model

import com.agile.android.leo.mvp.BaseModel
import com.leowong.project.eyepetizer.api.ApiManagerService
import com.leowong.project.eyepetizer.managers.RepositoryManager
import com.leowong.project.eyepetizer.mvp.contract.VideoDetailContract
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import io.reactivex.Observable

class VideoDetailModel : BaseModel(), VideoDetailContract.Model {
    override fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {
    }

    override fun requestRelatedVideo(id: Long): Observable<HomeBean.Issue> {
        return RepositoryManager.obtainRetrofitService(ApiManagerService::class.java).getRelatedData(id)
    }

    override fun onDestroy() {
    }
}