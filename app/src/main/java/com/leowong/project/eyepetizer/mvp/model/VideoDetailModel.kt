package com.leowong.project.eyepetizer.mvp.model

import com.leowong.project.eyepetizer.mvp.contract.VideoDetailContract
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean

class VideoDetailModel : VideoDetailContract.Model {
    override fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {
    }

    override fun requestRelatedVideo(id: Long) {
    }

    override fun onDestroy() {
    }
}