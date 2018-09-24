package com.leowong.project.eyepetizer.ui.activities

import android.os.Bundle
import com.agile.android.leo.mvp.IPresenter
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.BaseActivity
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.utils.StatusBarUtils
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_video_detail.*

class VideoDetailActivity : BaseActivity<IPresenter>() {
    /**
     * Item 详细数据
     */
    private lateinit var itemData: HomeBean.Issue.Item

    companion object {
        val BUNDLE_VIDEO_DATA = "video_data"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_video_detail
    }

    override fun initPresenter() {
    }

    override fun configViews() {
        StatusBarUtils.with(this).init()
        initSlide()
        val cover = itemData.data?.cover?.feed
        val coverOption = ImageLoaderOptions.Builder(vidoeCover, cover)
                .placeholder(R.drawable.placeholder_banner).isCrossFade(true).build()
        ImageLoader.showImage(coverOption)
    }

    override fun initData(savedInstanceState: Bundle?) {
        itemData = intent.getSerializableExtra(BUNDLE_VIDEO_DATA) as HomeBean.Issue.Item
    }

    override fun requestData() {
    }
}