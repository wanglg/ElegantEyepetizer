package com.leo.elegant.trunk.fragments

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View
import com.agile.android.leo.mvp.IPresenter
import com.android.leo.base.ui.fragments.BaseFragment
import com.leo.android.log.core.LogUtils
import com.leo.android.videoplayer.ijk.IRenderView
import com.leo.android.videoplayer.ijk.PlayerConfig
import com.leo.elegant.trunk.R
import com.leo.elegant.trunk.activity.MainActivity
import com.leo.elegant.trunk.adapter.SplashVideoTextAdapter
import kotlinx.android.synthetic.main.fragment_splash_video.*

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
        ijkvideo.setPlayerConfig(PlayerConfig.Builder().setLooping().disableAudioFocus()
                .setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT).build())
        ijkvideo.play()
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
    }

    override fun requestData() {
    }
}