package com.android.leo.toutiao.ui.adapter.provider

import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.ui.adapter.NewsListAdapter
import com.android.leo.toutiao.ui.adapter.entity.NewsFeedMultipleEntity
import com.chad.library.adapter.base.BaseViewHolder

class TextNewsItemProvider(mChannelCode: String) : BaseNewsItemProvider(mChannelCode) {
    override fun layout(): Int {
        return R.layout.item_text_news
    }

    override fun viewType(): Int {
        return NewsListAdapter.TEXT_NEWS
    }

    override fun convert(helper: BaseViewHolder, data: NewsFeedMultipleEntity, position: Int) {
        val item: News? = data.news;
        item?.let {
            convert(helper, it, position)
        }
    }
}