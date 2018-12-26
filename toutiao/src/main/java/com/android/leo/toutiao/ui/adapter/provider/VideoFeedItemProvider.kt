package com.android.leo.toutiao.ui.adapter.provider

import com.android.leo.toutiao.R
import com.android.leo.toutiao.ui.adapter.NewsListAdapter
import com.android.leo.toutiao.ui.adapter.entity.NewsFeedMultipleEntity
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions

class VideoFeedItemProvider(val mChannelCode: String) : BaseItemProvider<NewsFeedMultipleEntity, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_video_list
    }

    override fun viewType(): Int {
        return NewsListAdapter.VIDEO_FEED
    }

    override fun convert(helper: BaseViewHolder, data: NewsFeedMultipleEntity, position: Int) {
//        val videoView = helper.getView<IjkVideoView>(R.id.video_player)
        val news = data.news
//        videoView.setVideoPath("http://qc.cdn.kaiyanapp.com/1545199831687_e8a3c0a1.mp4?bfTime=5c1a82c0&bfKey=a864ee3ca68cd7169c71a11f2d7c9c26")
        if (news?.user_info != null && news.user_info.avatar_url != null) {
            val avatarOption = ImageLoaderOptions.Builder(helper.getView(R.id.iv_avatar), news.user_info.avatar_url).isCircle
                    .isCrossFade(true).build()
            ImageLoader.showImage(avatarOption)
        }
        if (news?.video_detail_info != null && news.video_detail_info.detail_video_large_image != null) {
            val coverOption = ImageLoaderOptions.Builder(helper.getView(R.id.vidoeCover), news.video_detail_info.detail_video_large_image.url)
                    .isCrossFade(true).build()
            ImageLoader.showImage(coverOption)
        }
        helper.setText(R.id.tv_author, news?.user_info?.name)//昵称
                .setText(R.id.tv_comment_count, news?.comment_count.toString())//评论数
        helper.setText(R.id.video_cover_title, news?.title)
        helper.addOnClickListener(R.id.video_play_img)
        helper.setVisible(R.id.vidoeCover, true)
        helper.setVisible(R.id.video_cover_layout, true)
        helper.setVisible(R.id.video_play_img, true)
        helper.setGone(R.id.item_loading_progress, false)
    }

    override fun onClick(helper: BaseViewHolder?, data: NewsFeedMultipleEntity?, position: Int) {
        super.onClick(helper, data, position)
    }
}