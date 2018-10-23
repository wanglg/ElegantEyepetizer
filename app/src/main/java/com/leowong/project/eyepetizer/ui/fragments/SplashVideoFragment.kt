package com.leowong.project.eyepetizer.ui.fragments

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View
import com.agile.android.leo.mvp.IPresenter
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.BaseFragment
import com.leowong.project.eyepetizer.media.ijk.IRenderView
import com.leowong.project.eyepetizer.media.ijk.PlayerConfig
import com.leowong.project.eyepetizer.ui.activities.MainActivity
import com.leowong.project.eyepetizer.ui.activities.SplashActivity
import com.leowong.project.eyepetizer.ui.adapters.SplashVideoTextAdapter
import com.leowong.project.eyepetizer.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_splash_video.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class SplashVideoFragment : BaseFragment<IPresenter>(), ViewPager.OnPageChangeListener, View.OnTouchListener {

    var startX = 0f
    var pagerPosition = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_splash_video
    }

    override fun initPresenter() {
    }

    override fun configViews() {
        ijkvideo.setAssertPath("landing.mp4")
        ijkvideo.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT)
        ijkvideo.setPlayerConfig(PlayerConfig.Builder().setLooping().disableAudioFocus().build())
        ijkvideo.startPlay()
        val adapter = SplashVideoTextAdapter()
        view_pager.adapter = adapter
        view_pager.addOnPageChangeListener(this)
        indicator.setViewPager(view_pager)
        skip.setOnClickListener {
            jump()
        }
        view_pager?.setOnTouchListener(this)
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
        ijkvideo.onPause()
    }

    override fun onPageSelected(position: Int) {
        if (position == 3) {
            skip.visibility = View.VISIBLE
        } else {
            skip.visibility = View.GONE
        }

    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        pagerPosition = position;
        if (position == 3) {
            LogUtils.d("offset-->" + positionOffset + "  positionOffsetPixels->" + positionOffsetPixels)
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (p1?.action == MotionEvent.ACTION_DOWN) {
            startX = p1.x;
        }
        if (p1?.action == MotionEvent.ACTION_UP) {
            val moveX = p1.x
            if (pagerPosition == 3 && startX - moveX > 200) {
                jump()
            }
        }
        return false

    }

    fun jump() {
        startActivity(MainActivity::class.java)
        activity?.finish()
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