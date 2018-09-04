package com.leowong.project.eyepetizer.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import com.agile.android.leo.mvp.IPresenter
import com.leowong.project.eyepetizer.MyApplication
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.BaseFragment
import com.leowong.project.eyepetizer.media.ijk.IRenderView
import kotlinx.android.synthetic.main.fragment_splash_video.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class SplashVideoFragment : BaseFragment<IPresenter>() {
    private var textTypeface: Typeface? = null

    private var descTypeFace: Typeface? = null
    private var boldTypeFace: Typeface? = null


    init {
//        textTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/lobster.otf")
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/Lobster.otf")
//        textTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        descTypeFace = Typeface.createFromAsset(MyApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        boldTypeFace = Typeface.createFromAsset(MyApplication.context.assets, "fonts/DIN-Condensed-Bold.ttf")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_splash_video
    }

    override fun initPresenter() {
    }

    override fun configViews() {
        ijkvideo.setAssertPath("landing.mp4")
        ijkvideo.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT)
//        ijkvideo.setVideoPath("http://cdn.yiwantv.com/video/480/4462_cb0055c80d905d978db1bc25d1c0449e.mp4?auth_key=1534521600-0-0-19a0c3e01f3904496f8d770838eb9d1f")
//        bottom.typeface=textTypeface
        val font :Typeface = Typeface.createFromAsset(activity?.assets,"fonts/Lobster.otf")
//        bottom.typeface=font
        ijkvideo.startPlay()
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
        ijkvideo.onPause()
    }

    override fun onFragmentResume(isFirst: Boolean, isViewDestroyed: Boolean) {
        super.onFragmentResume(isFirst, isViewDestroyed)
        ijkvideo.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ijkvideo.onDestory()
    }

    override fun initData(savedInstanceState: Bundle?) {
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
    }

    override fun requestData() {
    }
}