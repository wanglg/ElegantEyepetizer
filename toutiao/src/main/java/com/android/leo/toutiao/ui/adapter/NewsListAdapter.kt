package com.android.leo.toutiao.ui.adapter

import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.ui.adapter.entity.NewsFeedMultipleEntity
import com.android.leo.toutiao.ui.adapter.provider.*
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.leo.android.log.core.LogUtils

class NewsListAdapter(val mChannelCode: String, data: ArrayList<NewsFeedMultipleEntity>) : MultipleItemRvAdapter<NewsFeedMultipleEntity, BaseViewHolder>(data) {

    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(CenterPicNewsItemProvider(mChannelCode))
        mProviderDelegate.registerProvider(TextNewsItemProvider(mChannelCode))
        mProviderDelegate.registerProvider(ThreePicNewsItemProvider(mChannelCode))
        mProviderDelegate.registerProvider(RightPicNewsItemProvider(mChannelCode))
        mProviderDelegate.registerProvider(VideoFeedItemProvider(mChannelCode))
    }

    override fun getViewType(t: NewsFeedMultipleEntity): Int {
        return t.getItemType()
    }

    fun setData(items: ArrayList<News>) {
        mData = wrapList(items)
    }


    fun replaceData(item: ArrayList<News>) {
        replaceData(wrapList(item))
    }

    fun addItemData(item: ArrayList<News>) {
        addData(wrapList(item))
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        LogUtils.d("onViewDetachedFromWindow")
    }

    fun wrapList(itemList: List<News>): ArrayList<NewsFeedMultipleEntity> {
        val list: ArrayList<NewsFeedMultipleEntity> = ArrayList()
        for (item in itemList) {
            val homeMultipleEntity = NewsFeedMultipleEntity(item, mChannelCode)
            list.add(homeMultipleEntity)
            continue
        }
        return list
    }

    companion object {

        /**
         * 纯文字布局(文章、广告)
         */
        val TEXT_NEWS = 100
        /**
         * 居中大图布局(1.单图文章；2.单图广告；3.视频，中间显示播放图标，右侧显示时长)
         */
        val CENTER_SINGLE_PIC_NEWS = 200
        /**
         * 右侧小图布局(1.小图新闻；2.视频类型，右下角显示视频时长)
         */
        val RIGHT_PIC_VIDEO_NEWS = 300
        /**
         * 三张图片布局(文章、广告)
         */
        val THREE_PICS_NEWS = 400
        /**
         *视频feed
         */
        val VIDEO_FEED = 500

    }

}