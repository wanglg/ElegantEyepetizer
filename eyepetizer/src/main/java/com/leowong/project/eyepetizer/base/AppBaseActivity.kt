package com.leowong.project.eyepetizer.base

import android.annotation.SuppressLint
import android.graphics.Color
import com.agile.android.leo.mvp.IPresenter
import com.android.leo.base.ui.activities.BaseActivity
import com.leowong.project.eyepetizer.R
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition

/**
 * User: wanglg
 * Date: 2018-05-11
 * Time: 11:21
 * FIXME
 */
@SuppressLint("Registered")
abstract class AppBaseActivity<P : IPresenter> : BaseActivity<P>() {



    protected fun initSlide() {
//        val primary = resources.getColor(R.color.colorAccent)
//        val secondary = resources.getColor(R.color.translucence)
        val config = SlidrConfig.Builder()
//                .primaryColor(primary)
//                .secondaryColor(secondary)
                .scrimColor(Color.BLACK)
                .position(SlidrPosition.LEFT)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(5f)
                .distanceThreshold(.35f)
                .edge(true)
//                .listener(mSlidrListener)
                .build()
        Slidr.attach(this, config)
    }


}