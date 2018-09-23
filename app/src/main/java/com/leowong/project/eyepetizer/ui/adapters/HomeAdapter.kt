package com.leowong.project.eyepetizer.ui.adapters

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.ui.adapters.entity.HomeMultipleEntity
import com.leowong.project.eyepetizer.ui.adapters.provider.HomeVideoProvider

class HomeAdapter(data: ArrayList<HomeMultipleEntity>) : MultipleItemRvAdapter<HomeMultipleEntity, BaseViewHolder>(data) {


    // banner 作为 RecyclerView 的第一项
    var bannerItemSize = 0

    init {
        finishInitialize()
    }

    companion object {

        public const val ITEM_TYPE_BANNER = 1    //Banner 类型
        public const val ITEM_TYPE_TEXT_HEADER = 2   //textHeader
        public const val ITEM_TYPE_CONTENT: Int = 3    //item
    }

    /**
     * 设置 Banner 大小
     */
    fun setBannerSize(count: Int) {
        bannerItemSize = count
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


    /**
     * 得到 Item 的类型
     */
    /* override fun getItemViewType(position: Int): Int {
         return when {
             position == 0 ->
                 ITEM_TYPE_BANNER
             else ->
                 super.getItemViewType(position - 1)
         }
     }*/

    /**
     *  得到 RecyclerView Item 数量（Banner 作为一个 item）
     */
    /*  override fun getItemCount(): Int {
          return when {
              mData.size > bannerItemSize -> mData.size - bannerItemSize + 1
              mData.isEmpty() -> 0
              else -> 1
          }
      }*/

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(HomeVideoProvider())
    }

    override fun getViewType(t: HomeMultipleEntity): Int {
        return t.getItemType()
    }

}