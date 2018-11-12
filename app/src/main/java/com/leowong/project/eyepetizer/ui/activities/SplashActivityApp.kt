package com.leowong.project.eyepetizer.ui.activities

import android.os.Bundle
import com.agile.android.leo.mvp.IPresenter
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.AppBaseActivity
import com.leowong.project.eyepetizer.constants.PreferencesConstant
import com.leowong.project.eyepetizer.ui.fragments.SplashFragment
import com.leowong.project.eyepetizer.ui.fragments.SplashVideoFragment
import com.leowong.project.eyepetizer.utils.PreferencesUtil
import com.leowong.project.eyepetizer.utils.StatusBarUtils

class SplashActivityApp : AppBaseActivity<IPresenter>() {
    var isFirstLaunch = false;
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initPresenter() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun configViews() {
        window?.setBackgroundDrawable(null)
        StatusBarUtils.with(this).init()

        if (PreferencesUtil.readPreferences(this, PreferencesConstant.IS_FIRST_LAUNCH, true)) {
            PreferencesUtil.writePreferences(this, PreferencesConstant.IS_FIRST_LAUNCH, false)
            val sf = SplashVideoFragment()
            supportFragmentManager.beginTransaction().add(R.id.container, sf).commit()
        } else {
            val sf = SplashFragment()
            supportFragmentManager.beginTransaction().add(R.id.container, sf).commit()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        isFirstLaunch = PreferencesUtil.readPreferences(this, PreferencesConstant.IS_FIRST_LAUNCH, true)
    }

    override fun requestData() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}