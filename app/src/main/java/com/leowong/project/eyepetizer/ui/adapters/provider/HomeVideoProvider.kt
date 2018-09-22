package com.leowong.project.eyepetizer.ui.adapters.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.ui.adapters.HomeAdapter
import com.leowong.project.eyepetizer.ui.adapters.entity.HomeMultipleEntity

class HomeVideoProvider : BaseItemProvider<HomeMultipleEntity, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_home_content
    }

    override fun viewType(): Int {
        return HomeAdapter.ITEM_TYPE_CONTENT
    }

    override fun convert(helper: BaseViewHolder?, data: HomeMultipleEntity?, position: Int) {
    }
}