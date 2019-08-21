package com.agile.android.leo.base

import android.app.Application
import com.agile.android.leo.BuildConfig
import com.leo.android.log.core.LogUtils

/**
 * 此模块设计为和业务无关的技术框架
 * User: wanglg
 * Date: 2018-05-02
 * Time: 16:34
 * FIXME
 */
open class AgileApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LogUtils.init(this, BuildConfig.DEBUG)
    }

}