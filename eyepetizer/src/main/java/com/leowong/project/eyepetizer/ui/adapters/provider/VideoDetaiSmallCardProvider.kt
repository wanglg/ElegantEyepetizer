package com.leowong.project.eyepetizer.ui.adapters.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leowong.project.eyepetizer.R
import com.android.leo.base.durationFormat
import com.leowong.project.eyepetizer.events.VideoDetailItemClickEvent
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.ui.adapters.VideoDetailAdapter
import com.leowong.project.eyepetizer.ui.adapters.entity.VideoDetailMultipleEntity
import org.greenrobot.eventbus.EventBus

class VideoDetaiSmallCardProvider : BaseItemProvider<VideoDetailMultipleEntity, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_video_small_card
    }

    override fun viewType(): Int {
        return VideoDetailAdapter.ITEM_TYPE_CONTENT
    }

    override fun convert(holder: BaseViewHolder, vm: VideoDetailMultipleEntity, position: Int) {
        val data: HomeBean.Issue.Item? = vm.videoBean;
        with(holder) {
            setText(R.id.tv_title, data?.data?.title!!)
            setText(R.id.tv_tag, "#${data.data.category} / ${durationFormat(data.data.duration)}")
            val avatarOption = ImageLoaderOptions.Builder(holder.getView(R.id.iv_video_small_card), data.data.cover.detail)
                    .placeholder(R.drawable.placeholder_banner).build()
            ImageLoader.showImage(avatarOption)
        }
    }

    override fun onClick(helper: BaseViewHolder?, data: VideoDetailMultipleEntity, position: Int) {
        super.onClick(helper, data, position)
        EventBus.getDefault().post(VideoDetailItemClickEvent(data.videoBean!!))
    }
}