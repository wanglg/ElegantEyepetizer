package com.leowong.project.eyepetizer.ui.adapters.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.ui.adapters.VideoDetailAdapter
import com.leowong.project.eyepetizer.ui.adapters.entity.VideoDetailMultipleEntity

class VideoDetailFooterProvider : BaseItemProvider<VideoDetailMultipleEntity, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_footer
    }

    override fun viewType(): Int {
        return VideoDetailAdapter.ITEM_TYPE_FOOTER
    }

    override fun convert(helper: BaseViewHolder?, data: VideoDetailMultipleEntity?, position: Int) {
    }
}