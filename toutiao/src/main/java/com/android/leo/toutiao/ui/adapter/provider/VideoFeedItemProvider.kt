package com.android.leo.toutiao.ui.adapter.provider

import com.android.leo.toutiao.R
import com.android.leo.toutiao.ui.adapter.VideoListAdapter
import com.android.leo.toutiao.ui.adapter.entity.VideosFeedMultipleEntity
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider

class VideoFeedItemProvider : BaseItemProvider<VideosFeedMultipleEntity, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_video_list
    }

    override fun viewType(): Int {
        return VideoListAdapter.VIDEO_FEED
    }

    override fun convert(helper: BaseViewHolder?, data: VideosFeedMultipleEntity?, position: Int) {
    }
}