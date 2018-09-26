package com.leowong.project.eyepetizer.ui.activities

import android.os.Bundle
import android.view.View
import com.agile.android.leo.exception.ApiException
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.BaseActivity
import com.leowong.project.eyepetizer.media.SimpleMediaPlayerListener
import com.leowong.project.eyepetizer.mvp.contract.VideoDetailContract
import com.leowong.project.eyepetizer.mvp.model.VideoDetailModel
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.mvp.presenter.VideoDetailPresenter
import com.leowong.project.eyepetizer.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_video_detail.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class VideoDetailActivity : BaseActivity<VideoDetailPresenter>(), VideoDetailContract.View {
    override fun setVideo(url: String) {
        ijkvideo.setVideoPath(url)
        ijkvideo.startPlay()
    }

    override fun setVideoInfo(itemInfo: HomeBean.Issue.Item) {
    }

    override fun setBackground(url: String) {
        val imageLoaderOptions = ImageLoaderOptions.Builder(mVideoBackground, url).blurImage(true).isCrossFade(true).build()
        ImageLoader.showImage(imageLoaderOptions)
    }

    override fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {
    }

    override fun setErrorMsg(errorMsg: String) {
    }

    override fun resultError(exception: ApiException) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

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
        mPresenter = VideoDetailPresenter(VideoDetailModel(), this)
    }

    override fun onPause() {
        super.onPause()
        ijkvideo.onPause()
    }

    override fun onResume() {
        super.onResume()
        ijkvideo.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        ijkvideo.onDestory()
    }

    override fun configViews() {
        StatusBarUtils.with(this).init()
        initSlide()
        val cover = itemData.data?.cover?.feed
        val coverOption = ImageLoaderOptions.Builder(vidoeCover, cover)
                .placeholder(R.drawable.placeholder_banner).isCrossFade(true).build()
        ImageLoader.showImage(coverOption)
        ijkvideo.setMediaPlayerListener(object : SimpleMediaPlayerListener() {
            override fun onFirstFrameStart() {
                super.onFirstFrameStart()
                vidoeCover.visibility = View.GONE
            }
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        itemData = intent.getSerializableExtra(BUNDLE_VIDEO_DATA) as HomeBean.Issue.Item
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
    }

    override fun requestData() {
        mPresenter?.loadVideoInfo(itemData)
    }
}