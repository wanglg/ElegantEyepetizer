package com.leowong.project.client.ui.activities

import com.agile.android.leo.mvp.IPresenter
import com.leowong.project.client.base.BaseActivity
import com.leowong.project.client.utils.ActivityUtil
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * 启动页 闪屏页
 * Created by caowt on 2018/1/4 0004.
 */
class SplashActivity : BaseActivity<IPresenter>() {
    override fun getLayoutId(): Int {
        return 0
    }

    override fun initPresenter() {

    }

    override fun setContentView(layoutResID: Int) {
    }

    override fun configViews() {
    }

    override fun initData() {
        startAPP()
    }


    private fun startAPP() {
//        if (SharedPreferencesUtils.instance.getValue(GlobalConstants.IS_FIRST_LAUNCH, GlobalConstants.BOOLEAN_TRUE) as Boolean) {
//            Observable.timer(2000, TimeUnit.MILLISECONDS).compose(bindToLifecycle())
//                    .subscribe({
//                        if (!isFinishing) {
//                            ActivityUtil.switchTo(this, GuideActivity::class.java)
//                            SharedPreferencesUtils.instance.put(GlobalConstants.IS_FIRST_LAUNCH, false)
//                            finish()
//                        }
//                    })
//
//        } else {
        Observable.timer(2000, TimeUnit.MILLISECONDS).compose(bindToLifecycle())
                .subscribe({
                    if (!isFinishing) {
                        ActivityUtil.switchTo(this, MainActivity::class.java)
                        finish()
                    }
                })
    }

//    }


}
