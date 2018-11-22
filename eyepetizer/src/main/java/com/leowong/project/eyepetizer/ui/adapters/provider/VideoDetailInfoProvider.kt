package com.leowong.project.eyepetizer.ui.adapters.provider

import android.widget.TextView
import com.android.leo.base.BaseApplication
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leowong.project.eyepetizer.R
import com.android.leo.base.durationFormat
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.ui.adapters.VideoDetailAdapter
import com.leowong.project.eyepetizer.ui.adapters.entity.VideoDetailMultipleEntity

class VideoDetailInfoProvider : BaseItemProvider<VideoDetailMultipleEntity, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_video_detail_info
    }

    override fun viewType(): Int {
        return VideoDetailAdapter.ITEM_TYPE_DETAIL_INFO
    }

    override fun convert(helper: BaseViewHolder, vm: VideoDetailMultipleEntity, position: Int) {
        val data: HomeBean.Issue.Item? = vm.videoBean;
        data?.data?.title?.let { helper.setText(R.id.tv_title, it) }
        //视频简介
        data?.data?.description?.let { helper.setText(R.id.expandable_text, it) }
        //标签
        helper.setText(R.id.tv_tag, "#${data?.data?.category} / ${durationFormat(data?.data?.duration)}")
        //喜欢
        helper.setText(R.id.tv_action_favorites, data?.data?.consumption?.collectionCount.toString())
        //分享
        helper.setText(R.id.tv_action_share, data?.data?.consumption?.shareCount.toString())
        //评论
        helper.setText(R.id.tv_action_reply, data?.data?.consumption?.replyCount.toString())

        if (data?.data?.author != null) {
            with(helper) {
                setText(R.id.tv_author_name, data.data.author.name)
                setText(R.id.tv_author_desc, data.data.author.description)
                val avatarOption = ImageLoaderOptions.Builder(helper.getView(R.id.iv_avatar), data.data.author.icon)
                        .placeholder(R.mipmap.default_avatar).isCircle.isCrossFade(true).build()
                ImageLoader.showImage(avatarOption)
            }
        } else {
            helper.setGone(R.id.layout_author_view, false)
        }

        with(helper) {
            getView<TextView>(R.id.tv_action_favorites).setOnClickListener {
                android.widget.Toast.makeText(BaseApplication.context, "喜欢", android.widget.Toast.LENGTH_SHORT).show()
            }
            getView<TextView>(R.id.tv_action_share).setOnClickListener {
                android.widget.Toast.makeText(BaseApplication.context, "分享", android.widget.Toast.LENGTH_SHORT).show()
            }
            getView<TextView>(R.id.tv_action_reply).setOnClickListener {
                android.widget.Toast.makeText(BaseApplication.context, "评论", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}