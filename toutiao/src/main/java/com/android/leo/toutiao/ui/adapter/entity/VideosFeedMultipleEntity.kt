package com.android.leo.toutiao.ui.adapter.entity

import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.ui.adapter.VideoListAdapter.Companion.VIDEO_FEED
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

class VideosFeedMultipleEntity : MultiItemEntity, Serializable {
    var news: News? = null

    constructor(news: News) {
        this.news = news
    }

    override fun getItemType(): Int {
        return VIDEO_FEED
    }


}