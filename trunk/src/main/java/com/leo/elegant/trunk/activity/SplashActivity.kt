package com.leo.elegant.trunk.activity

import android.os.Bundle
import com.agile.android.leo.mvp.IPresenter
import com.android.leo.base.ui.activities.BaseActivity
import com.android.leo.base.utils.PreferencesUtil
import com.gyf.immersionbar.ImmersionBar
import com.leo.elegant.trunk.R
import com.leo.elegant.trunk.fragments.SplashFragment
import com.leo.elegant.trunk.fragments.SplashVideoFragment

class SplashActivity : BaseActivity<IPresenter>() {
    val IS_FIRST_LAUNCH = "is_first_launch"
    var isFirstLaunch = false;
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initPresenter() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun configViews() {
        window?.setBackgroundDrawable(null)
        ImmersionBar.with(this).init()

        if (isFirstLaunch) {
            PreferencesUtil.writePreferences(this, IS_FIRST_LAUNCH, false)
            val sf = SplashVideoFragment()
            supportFragmentManager.beginTransaction().add(R.id.container, sf).commit()
        } else {
            val sf = SplashFragment()
            supportFragmentManager.beginTransaction().add(R.id.container, sf).commit()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        isFirstLaunch = PreferencesUtil.readBoolean(this, IS_FIRST_LAUNCH, true)
    }

    override fun requestData() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}