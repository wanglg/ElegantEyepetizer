package com.leowong.project.eyepetizer.ui.adapters

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.ui.adapters.entity.VideoDetailMultipleEntity
import com.leowong.project.eyepetizer.ui.adapters.provider.VideoDetaiSmallCardProvider
import com.leowong.project.eyepetizer.ui.adapters.provider.VideoDetaiTextCardProvider
import com.leowong.project.eyepetizer.ui.adapters.provider.VideoDetailFooterProvider
import com.leowong.project.eyepetizer.ui.adapters.provider.VideoDetailInfoProvider

class VideoDetailAdapter(data: ArrayList<VideoDetailMultipleEntity>) : MultipleItemRvAdapter<VideoDetailMultipleEntity, BaseViewHolder>(data) {


    companion object {

        public const val ITEM_TYPE_DETAIL_INFO = 1
        public const val ITEM_TYPE_TEXT_HEADER = 2   //textHeader
        public const val ITEM_TYPE_CONTENT: Int = 3    //item
        public const val ITEM_TYPE_FOOTER: Int = 4    //footer
    }

    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(VideoDetailInfoProvider())
        mProviderDelegate.registerProvider(VideoDetaiSmallCardProvider())
        mProviderDelegate.registerProvider(VideoDetaiTextCardProvider())
        mProviderDelegate.registerProvider(VideoDetailFooterProvider())
    }


    /**
     * 添加相关推荐等数据 Item
     */
    fun addItemData(item: ArrayList<HomeBean.Issue.Item>) {
        addData(wrapList(item))
        addData(VideoDetailMultipleEntity(ITEM_TYPE_FOOTER))
    }

    /**
     * 添加视频的详细信息
     */
    fun addItemData(item: HomeBean.Issue.Item) {
        mData.clear()
        mData.add(VideoDetailMultipleEntity(item, ITEM_TYPE_DETAIL_INFO))
        notifyDataSetChanged()
    }

    fun wrapList(itemList: List<HomeBean.Issue.Item>): ArrayList<VideoDetailMultipleEntity> {
        val list: ArrayList<VideoDetailMultipleEntity> = ArrayList()
        for ((index, item) in itemList.withIndex()) {
            val homeMultipleEntity = VideoDetailMultipleEntity(item, if (item.type == "textCard") {
                ITEM_TYPE_TEXT_HEADER
            } else {
                ITEM_TYPE_CONTENT
            })
            list.add(homeMultipleEntity)
            continue

        }
        return list
    }

    override fun getViewType(t: VideoDetailMultipleEntity): Int {
        return t.getItemType()
    }
}