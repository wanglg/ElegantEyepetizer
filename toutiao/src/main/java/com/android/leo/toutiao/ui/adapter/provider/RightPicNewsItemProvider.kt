package com.android.leo.toutiao.ui.adapter.provider

import com.agile.android.leo.utils.TimeUtils
import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.ui.adapter.NewsListAdapter
import com.android.leo.toutiao.ui.adapter.entity.NewsFeedMultipleEntity
import com.chad.library.adapter.base.BaseViewHolder
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions

class RightPicNewsItemProvider(mChannelCode: String) : BaseNewsItemProvider(mChannelCode) {
    override fun convert(helper: BaseViewHolder, data: NewsFeedMultipleEntity, position: Int) {
        val item: News? = data.news;
        item?.let {
            convert(helper, it, position)
            //右侧小图布局，判断是否有视频
            if (it.has_video) {
                helper.setVisible(R.id.ll_duration, true)//显示时长
                helper.setText(R.id.tv_duration, TimeUtils.secToTime(it.video_duration))//设置时长
            } else {
                helper.setVisible(R.id.ll_duration, false)//隐藏时长
            }
            val imageLoaderOptions = ImageLoaderOptions.Builder(helper.getView(R.id.iv_img), it.middle_image.url).build()
            ImageLoader.showImage(imageLoaderOptions)
        }
    }

    override fun layout(): Int {
        return R.layout.item_pic_video_news
    }

    override fun viewType(): Int {
        return NewsListAdapter.RIGHT_PIC_VIDEO_NEWS
    }
}