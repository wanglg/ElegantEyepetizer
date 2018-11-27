package com.shortvideo.android.leo.ui.adapters

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leowang.shortvideo.R
import com.shortvideo.android.leo.mvp.model.entity.VideoBean

class ShortVideoTabAdapter(data: ArrayList<VideoBean>) : BaseQuickAdapter<VideoBean, BaseViewHolder>(R.layout.sv_item_short_video_feed, data) {
    override fun convert(helper: BaseViewHolder, item: VideoBean) {
        helper.setText(R.id.videoTitleText, item.title)
        val coverOption = ImageLoaderOptions.Builder(helper.getView(R.id.videoCover), item.thumb)
                .build()
        val cover = helper.getView<ImageView>(R.id.videoCover)
        if (item.thumbScaleType == 0) {
            cover.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            cover.scaleType = ImageView.ScaleType.FIT_CENTER
        }
        ImageLoader.showImage(coverOption)
    }
}