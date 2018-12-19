package com.android.leo.toutiao.ui.adapter.provider

import com.android.leo.toutiao.R
import com.android.leo.toutiao.ui.adapter.VideoListAdapter
import com.android.leo.toutiao.ui.adapter.entity.VideosFeedMultipleEntity
import com.android.leo.toutiao.ui.widget.VideoFeedItemController
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.leo.android.videplayer.IjkVideoView

class VideoFeedItemProvider : BaseItemProvider<VideosFeedMultipleEntity, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_video_list
    }

    override fun viewType(): Int {
        return VideoListAdapter.VIDEO_FEED
    }

    override fun convert(helper: BaseViewHolder, data: VideosFeedMultipleEntity, position: Int) {
        val videoView = helper.getView<IjkVideoView>(R.id.video_player)
        val news = data.news
        if (!videoView.haveControlView()) {
            val controlView = VideoFeedItemController(mContext)
            controlView.setNew(news!!)
            videoView.attachMediaControl(controlView)
        }
    }
}