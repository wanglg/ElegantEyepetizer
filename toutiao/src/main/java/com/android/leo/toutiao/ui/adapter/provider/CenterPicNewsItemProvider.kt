package com.android.leo.toutiao.ui.adapter.provider

import android.widget.TextView
import com.agile.android.leo.utils.TimeUtils
import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.ui.adapter.NewsListAdapter
import com.android.leo.toutiao.ui.adapter.entity.NewsFeedMultipleEntity
import com.chad.library.adapter.base.BaseViewHolder
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions

class CenterPicNewsItemProvider(mChannelCode: String) : BaseNewsItemProvider(mChannelCode) {
    override fun convert(helper: BaseViewHolder, data: NewsFeedMultipleEntity, position: Int) {
        val item: News? = data.news;
        item?.let {
            convert(helper, it, position)
            //中间大图布局，判断是否有视频
            val tvBottomRight = helper.getView<TextView>(R.id.tv_bottom_right)
            if (it.has_video) {
                helper.setVisible(R.id.iv_play, true)//显示播放按钮
                tvBottomRight.setCompoundDrawables(null, null, null, null)//去除TextView左侧图标
                helper.setText(R.id.tv_bottom_right, TimeUtils.secToTime(it.video_duration))//设置时长
                val imageLoaderOptions = ImageLoaderOptions.Builder(helper.getView(R.id.iv_img), it.video_detail_info.detail_video_large_image.url).build()
                ImageLoader.showImage(imageLoaderOptions)
            } else {
                helper.setVisible(R.id.iv_play, false)//隐藏播放按钮
                if (it.gallary_image_count == 1) {
                    tvBottomRight.setCompoundDrawables(null, null, null, null)//去除TextView左侧图标
                } else {
                    tvBottomRight.setCompoundDrawables(mContext.resources.getDrawable(R.mipmap.icon_picture_group), null, null, null)//TextView增加左侧图标
                    helper.setText(R.id.tv_bottom_right, it.gallary_image_count.toString() + mContext.getString(R.string.img_unit))//设置图片数
                }
                val imageLoaderOptions = ImageLoaderOptions.Builder(helper.getView(R.id.iv_img), it.image_list.get(0).url.replace("list/300x196", "large")).build()
                ImageLoader.showImage(imageLoaderOptions)
            }
        }
    }

    override fun layout(): Int {
        return R.layout.item_center_pic_news
    }

    override fun viewType(): Int {
        return NewsListAdapter.CENTER_SINGLE_PIC_NEWS
    }
}