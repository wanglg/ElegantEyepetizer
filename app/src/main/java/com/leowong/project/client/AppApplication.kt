package com.leowong.project.client

import com.agile.android.leo.base.AgileApplication

/**
 * 全局 application
 * Created by caowt on 2017/12/26 0026.
 */

class AppApplication : AgileApplication() {

    override fun onCreate() {
        super.onCreate()
//        initLeakCanary()
        instance = this
    }


    companion object {
        var instance: AppApplication? = null
            private set
    }


}
