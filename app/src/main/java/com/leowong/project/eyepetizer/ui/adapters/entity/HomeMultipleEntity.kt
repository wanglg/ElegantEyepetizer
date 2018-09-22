package com.leowong.project.eyepetizer.ui.adapters.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

class HomeMultipleEntity : MultiItemEntity {
    var itemType: Int? = 0;
    override fun getItemType(): Int {
        return itemType!!
    }
}