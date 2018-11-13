package com.android.leo.base.delegate

import android.app.Application
import android.content.Context
import android.support.annotation.NonNull
import com.android.leo.base.integration.ConfigModule
import com.android.leo.base.integration.ManifestParser
import java.util.ArrayList

class AppDelegate : AppLifecycles {
    private var mApplication: Application? = null
    private var mModules: List<ConfigModule>? = null
    private var mAppLifecycles: List<AppLifecycles> = ArrayList()
    private var mActivityLifecycles: List<Application.ActivityLifecycleCallbacks>? = ArrayList()

    constructor(@NonNull context: Context) {
        mModules = ManifestParser(context).parse()
        mModules?.let {
            for (module in it) {
                module.injectAppLifecycle(context, mAppLifecycles)
                module.injectActivityLifecycle(context, mActivityLifecycles)
            }
        }

    }

    override fun attachBaseContext(base: Context) {
        //遍历 mAppLifecycles, 执行所有已注册的 AppLifecycles 的 attachBaseContext() 方法 (框架外部, 开发者扩展的逻辑)
        for (lifecycle in mAppLifecycles) {
            lifecycle.attachBaseContext(base)
        }
    }

    override fun onCreate(mApplication: Application) {


        //执行框架外部, 开发者扩展的 App onCreate 逻辑
        for (lifecycle in mAppLifecycles) {
            lifecycle.onCreate(mApplication)
        }
    }

    override fun onTerminate(application: Application) {
        mApplication = null
    }
}