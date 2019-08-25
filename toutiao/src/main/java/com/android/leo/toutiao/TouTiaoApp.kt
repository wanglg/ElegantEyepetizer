package com.android.leo.toutiao

import android.app.Application
import android.content.Context
import com.android.leo.base.BaseApplication
import com.android.leo.base.delegate.AppLifecycles
import com.leo.android.log.core.LogUtils
import com.tencent.smtt.sdk.QbSdk
import kotlin.properties.Delegates

class TouTiaoApp : AppLifecycles {
    val mChannelCodes by lazy {
        BaseApplication.context.resources.getStringArray(R.array.channel_code)
    }

    companion object {

        private val TAG = "BaseApplication"

        var context: TouTiaoApp by Delegates.notNull()
            private set
    }

    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
        context = this
        initX5(application)
    }

    override fun onTerminate(application: Application) {
    }

    private fun initX5(application: Application) {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        LogUtils.d("app", " initX5")
        val cb = object : QbSdk.PreInitCallback {

            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.d("app", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {}
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(application, cb)
    }

}