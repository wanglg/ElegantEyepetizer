package com.leowong.project.eyepetizer.ui.adapters.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.ui.adapters.VideoDetailAdapter
import com.leowong.project.eyepetizer.ui.adapters.entity.VideoDetailMultipleEntity

class VideoDetaiTextCardProvider : BaseItemProvider<VideoDetailMultipleEntity, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_video_text_card
    }

    override fun viewType(): Int {
        return VideoDetailAdapter.ITEM_TYPE_TEXT_HEADER
    }

    override fun convert(helper: BaseViewHolder, vm: VideoDetailMultipleEntity, position: Int) {
        val data: HomeBean.Issue.Item? = vm.videoBean;
        helper.setText(R.id.tv_text_card, data?.data?.text!!)
        //设置方正兰亭细黑简体
//        holder.getView<TextView>(R.id.tv_text_card).typeface =textTypeface
    }
}