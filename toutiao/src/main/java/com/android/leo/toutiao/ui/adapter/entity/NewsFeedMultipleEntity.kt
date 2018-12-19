package com.android.leo.toutiao.ui.adapter.entity

import android.text.TextUtils
import com.agile.android.leo.utils.ListUtils
import com.android.leo.toutiao.TouTiaoApp
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.ui.adapter.NewsListAdapter.Companion.CENTER_SINGLE_PIC_NEWS
import com.android.leo.toutiao.ui.adapter.NewsListAdapter.Companion.RIGHT_PIC_VIDEO_NEWS
import com.android.leo.toutiao.ui.adapter.NewsListAdapter.Companion.TEXT_NEWS
import com.android.leo.toutiao.ui.adapter.NewsListAdapter.Companion.THREE_PICS_NEWS
import com.android.leo.toutiao.ui.adapter.NewsListAdapter.Companion.VIDEO_FEED
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

class NewsFeedMultipleEntity : MultiItemEntity, Serializable {
    var itemType: Int? = 0;
    var mChannelCode: String? = null;
    var news: News? = null

    constructor(news: News) {
        this.news = news
        itemType = getViewType(news)
    }

    constructor(news: News, mChannelCode: String) {
        this.news = news
        this.mChannelCode = mChannelCode
        itemType = getViewType(news)
    }

    override fun getItemType(): Int {
        return itemType!!
    }

    protected fun getViewType(news: News): Int {
        val videoCode = TouTiaoApp.context.mChannelCodes[1]
        if (TextUtils.equals(mChannelCode, videoCode)) {
            return VIDEO_FEED
        }
        if (news.has_video) {
            //如果有视频
            if (news.video_style == 0) {
                //右侧视频
                return if (news.middle_image == null || TextUtils.isEmpty(news.middle_image.url)) {
                    TEXT_NEWS
                } else {
                    RIGHT_PIC_VIDEO_NEWS
                }
            } else if (news.video_style == 2) {
                //居中视频
                return CENTER_SINGLE_PIC_NEWS
            }
        } else {
            //非视频新闻
            if (!news.has_image) {
                //纯文字新闻
                return TEXT_NEWS
            } else {
                if (ListUtils.isEmpty(news.image_list)) {
                    //图片列表为空，则是右侧图片
                    return RIGHT_PIC_VIDEO_NEWS
                }

                return if (news.gallary_image_count == 3) {
                    //图片数为3，则为三图
                    THREE_PICS_NEWS
                } else CENTER_SINGLE_PIC_NEWS

                //中间大图，右下角显示图数
            }
        }

        return TEXT_NEWS
    }

}