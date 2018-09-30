package com.leowong.project.eyepetizer.ui.adapters.entity

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import java.io.Serializable

class VideoDetailMultipleEntity : MultiItemEntity, Serializable {
    var videoBean: HomeBean.Issue.Item? = null
    var itemType: Int? = 0;

    constructor(videoBean: HomeBean.Issue.Item, itemType: Int) {
        this.videoBean = videoBean
        this.itemType = itemType
    }

    constructor(itemType: Int?) {
        this.itemType = itemType
    }


    override fun getItemType(): Int {
        return itemType!!
    }
}