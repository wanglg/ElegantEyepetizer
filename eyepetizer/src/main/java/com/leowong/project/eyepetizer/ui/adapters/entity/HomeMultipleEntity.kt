package com.leowong.project.eyepetizer.ui.adapters.entity

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import java.io.Serializable

class HomeMultipleEntity : MultiItemEntity ,Serializable{
    var homeBean: HomeBean.Issue.Item? = null
    var homeBeanList: List<HomeBean.Issue.Item>? = null
    var itemType: Int? = 0;

    constructor(homeBean: HomeBean.Issue.Item, itemType: Int) {
        this.homeBean = homeBean
        this.itemType = itemType
    }

    constructor(itemType: Int?) {
        this.itemType = itemType
    }

    constructor(homeBeanList: List<HomeBean.Issue.Item>, itemType: Int) {
        this.homeBeanList = homeBeanList
        this.itemType = itemType
    }

    override fun getItemType(): Int {
        return itemType!!
    }
}