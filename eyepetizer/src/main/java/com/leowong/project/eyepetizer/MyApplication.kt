package com.leowong.project.eyepetizer

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.agile.android.leo.base.AgileApplication
import com.agile.android.leo.utils.LogUtils
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderConfig
import com.lasingwu.baselibrary.LoaderEnum
import com.android.leo.base.glide.GlideImageLoader
import com.leowong.project.eyepetizer.managers.NetworkManager
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kotlin.properties.Delegates

class MyApplication : AgileApplication() {
    private var refWatcher: RefWatcher? = null

    companion object {

        private val TAG = "MyApplication"

        var context: Context by Delegates.notNull()
            private set

        fun getRefWatcher(context: Context): RefWatcher? {
            val myApplication = context.applicationContext as MyApplication
            return myApplication.refWatcher
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        refWatcher = setupLeakCanary()
        NetworkManager.instance.registerReceiver(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        initImageLoader()
    }

    fun initImageLoader() {
        val config = ImageLoaderConfig.Builder(LoaderEnum.GLIDE, GlideImageLoader())
                .maxMemory(40 * 1024 * 1024L)  // 单位为Byte
                .build()
        ImageLoader.init(this, config)
    }

    private fun setupLeakCanary(): RefWatcher {
        return if (LeakCanary.isInAnalyzerProcess(this)) {
            RefWatcher.DISABLED
        } else {
            LeakCanary.install(this)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkManager.instance.unregisterReceiver(this)
    }

    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            LogUtils.d(TAG, "onCreated: " + activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }


        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            LogUtils.d(TAG, "onDestroy: " + activity.componentName.className)
        }
    }
}