package com.leowong.project.eyepetizer.ui.adapters.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leowong.project.eyepetizer.R
import com.android.leo.base.durationFormat
import com.android.leo.base.getScreenWidth
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.ui.adapters.HomeAdapter
import com.leowong.project.eyepetizer.ui.adapters.entity.HomeMultipleEntity

class HomeVideoProvider : BaseItemProvider<HomeMultipleEntity, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_home_content
    }

    override fun viewType(): Int {
        return HomeAdapter.ITEM_TYPE_CONTENT
    }

    override fun convert(helper: BaseViewHolder, data: HomeMultipleEntity, position: Int) {
        val item: HomeBean.Issue.Item? = data.homeBean;
        val itemData = item?.data

        val defAvatar = R.mipmap.default_avatar
        val cover = itemData?.cover?.feed
        var avatar = itemData?.author?.icon
        var tagText: String? = "#"

        // 作者出处为空，就显获取提供者的信息
        if (avatar.isNullOrEmpty()) {
            avatar = itemData?.provider?.icon
        }
        // 加载封页图
        val imageWidth = getScreenWidth(mContext) - (mContext.resources.getDimensionPixelOffset(R.dimen.spacing_normal) * 2)
        val imageHeight = imageWidth * 0.6
        val coverOption = ImageLoaderOptions.Builder(helper.getView(R.id.iv_cover_feed), cover).override(imageWidth, imageHeight.toInt())
                .placeholder(R.drawable.placeholder_banner).isCrossFade(true).build()
        ImageLoader.showImage(coverOption)
        // 如果提供者信息为空，就显示默认
        if (avatar.isNullOrEmpty()) {
            val avatarOption = ImageLoaderOptions.Builder(helper.getView(R.id.iv_avatar), defAvatar)
                    .placeholder(R.mipmap.default_avatar).isCircle().isCrossFade(true).build()
            ImageLoader.showImage(avatarOption)

        } else {
            val avatarOption = ImageLoaderOptions.Builder(helper.getView(R.id.iv_avatar), avatar)
                    .placeholder(R.mipmap.default_avatar).isCircle().isCrossFade(true).build()
            ImageLoader.showImage(avatarOption)
        }
        helper.setText(R.id.tv_title, itemData?.title ?: "")

        //遍历标签
        itemData?.tags?.take(4)?.forEach {
            tagText += (it.name + "/")
        }
        // 格式化时间
        val timeFormat = durationFormat(itemData?.duration)

        tagText += timeFormat

        helper.setText(R.id.tv_tag, tagText!!)

        helper.setText(R.id.tv_category, "#" + itemData?.category)

        /*  holder.setOnItemClickListener(listener = View.OnClickListener {
              goToVideoPlayer(mContext as Activity, holder.getView(R.id.iv_cover_feed), item)
          })*/
    }
}