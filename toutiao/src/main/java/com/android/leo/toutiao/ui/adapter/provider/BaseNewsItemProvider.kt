package com.android.leo.toutiao.ui.adapter.provider

import android.text.TextUtils
import com.agile.android.leo.utils.TimeUtils
import com.android.leo.base.BaseApplication
import com.android.leo.toutiao.Constant
import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.ui.adapter.entity.NewsFeedMultipleEntity
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider

/**
 *
 */
abstract class BaseNewsItemProvider(val mChannelCode: String) : BaseItemProvider<NewsFeedMultipleEntity, BaseViewHolder>() {
    private val mChannelCodes by lazy {
        BaseApplication.context.resources.getStringArray(R.array.channel_code)
    }

    protected fun convert(helper: BaseViewHolder, news: News, position: Int) {
        helper.setText(R.id.tv_title, news.title)
                .setText(R.id.tv_author, news.source)
                .setText(R.id.tv_comment_num, news.comment_count.toString() + (mContext.resources.getString(R.string.comment)))
                .setText(R.id.tv_time, TimeUtils.getShortTime(news.behot_time * 1000));
        //根据情况显示置顶、广告和热点的标签
        val isTop = position == 0 && mChannelCode == mChannelCodes[0] //属于置顶
        val isHot = news.hot == 1//属于热点新闻
        val isAD = if (!TextUtils.isEmpty(news.tag)) news.tag.equals(Constant.ARTICLE_GENRE_AD) else false//属于广告新闻
        val isMovie = if (!TextUtils.isEmpty(news.tag)) news.tag.equals(Constant.TAG_MOVIE) else false//如果是影视
        helper.setVisible(R.id.tv_tag, isTop || isHot || isAD)//如果是上面任意一个，显示标签
        helper.setVisible(R.id.tv_comment_num, !isAD)//如果是广告，则隐藏评论数

        var tag = ""
        if (isTop) {
            tag = mContext.resources.getString(R.string.to_top)
            helper.setTextColor(R.id.tv_tag, mContext.resources.getColor(R.color.color_F96B6B))
        } else if (isHot) {
            tag = mContext.resources.getString(R.string.hot)
            helper.setTextColor(R.id.tv_tag, mContext.resources.getColor(R.color.color_F96B6B))
        } else if (isAD) {
            tag = mContext.resources.getString(R.string.ad)
            helper.setTextColor(R.id.tv_tag, mContext.resources.getColor(R.color.color_3091D8))
        } else if (isMovie) {
            //如果是影视
            tag = mContext.resources.getString(R.string.tag_movie)
            helper.setTextColor(R.id.tv_tag, mContext.resources.getColor(R.color.color_F96B6B))
        }
        helper.setText(R.id.tv_tag, tag)

    }
}