package com.android.leo.toutiao.ui.adapter

import com.android.leo.toutiao.ui.adapter.entity.VideosFeedMultipleEntity
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter

class VideoListAdapter(val mChannelCode: String, data: ArrayList<VideosFeedMultipleEntity>) : MultipleItemRvAdapter<VideosFeedMultipleEntity, BaseViewHolder>(data) {
    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
    }

    override fun getViewType(t: VideosFeedMultipleEntity): Int {
        return t.getItemType()
    }

    companion object {
        val VIDEO_FEED = 1000
    }
}