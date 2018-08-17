package com.leowong.project.eyepetizer.ui.fragments

import android.os.Bundle
import com.agile.android.leo.mvp.IPresenter
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_splash_video.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class SplashVideoFragment : BaseFragment<IPresenter>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_splash_video
    }

    override fun initPresenter() {
    }

    override fun configViews() {
        ijkvideo.setAssertPath("landing.mp4")
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