package com.android.leo.toutiao.ui.adapter.provider

import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.ui.adapter.NewsListAdapter
import com.android.leo.toutiao.ui.adapter.entity.NewsFeedMultipleEntity
import com.chad.library.adapter.base.BaseViewHolder
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions

class ThreePicNewsItemProvider(mChannelCode: String) : BaseNewsItemProvider(mChannelCode) {
    override fun convert(helper: BaseViewHolder, data: NewsFeedMultipleEntity, position: Int) {
        val item: News? = data.news;
        item?.let {
            convert(helper, it, position)
            val imageLoaderOptions1 = ImageLoaderOptions.Builder(helper.getView(R.id.iv_img1), it.image_list.get(0).url).build()
            ImageLoader.showImage(imageLoaderOptions1)
            val imageLoaderOptions2 = ImageLoaderOptions.Builder(helper.getView(R.id.iv_img2), it.image_list.get(1).url).build()
            ImageLoader.showImage(imageLoaderOptions2)
            val imageLoaderOptions3 = ImageLoaderOptions.Builder(helper.getView(R.id.iv_img3), it.image_list.get(2).url).build()
            ImageLoader.showImage(imageLoaderOptions3)
        }
    }

    override fun layout(): Int {
        return R.layout.item_three_pics_news
    }

    override fun viewType(): Int {
        return NewsListAdapter.THREE_PICS_NEWS
    }
}