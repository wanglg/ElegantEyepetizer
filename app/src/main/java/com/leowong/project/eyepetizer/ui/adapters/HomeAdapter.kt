package com.leowong.project.eyepetizer.ui.adapters

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.ui.adapters.entity.HomeMultipleEntity
import com.leowong.project.eyepetizer.ui.adapters.provider.HomeVideoProvider

class HomeAdapter(data: ArrayList<HomeMultipleEntity>) : MultipleItemRvAdapter<HomeMultipleEntity, BaseViewHolder>(data) {



    init {
        finishInitialize()
    }

    companion object {

        public const val ITEM_TYPE_BANNER = 1    //Banner 类型
        public const val ITEM_TYPE_TEXT_HEADER = 2   //textHeader
        public const val ITEM_TYPE_CONTENT: Int = 3    //item
    }


    /**
     * 添加更多数据
     */
    fun addItemData(itemList: ArrayList<HomeBean.Issue.Item>) {
        addData(wrapList(itemList))
    }

    fun wrapList(itemList: List<HomeBean.Issue.Item>): ArrayList<HomeMultipleEntity> {
        val list: ArrayList<HomeMultipleEntity> = ArrayList()
        for (item in itemList) {
            val homeMultipleEntity = HomeMultipleEntity(item, ITEM_TYPE_CONTENT)
            list.add(homeMultipleEntity)
        }
        return list
    }

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(HomeVideoProvider())
    }

    override fun getViewType(t: HomeMultipleEntity): Int {
        return t.getItemType()
    }

}