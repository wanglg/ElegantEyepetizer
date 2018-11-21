package com.android.leo.base

import android.content.Context
import com.agile.android.leo.base.AgileApplication
import com.android.leo.base.delegate.AppDelegate
import com.android.leo.base.delegate.AppLifecycles
import com.android.leo.base.glide.GlideImageLoader
import com.android.leo.base.manager.NetworkManager
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderConfig
import com.lasingwu.baselibrary.LoaderEnum
import kotlin.properties.Delegates

open class BaseApplication : AgileApplication() {

    private var mAppDelegate: AppLifecycles? = null

    companion object {

        private val TAG = "BaseApplication"

        var context: Context by Delegates.notNull()
            private set
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null) {
            mAppDelegate = AppDelegate(base)
            mAppDelegate?.attachBaseContext(base)
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        mAppDelegate?.onCreate(this)
        NetworkManager.instance.registerReceiver(this)
        initImageLoader()
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate?.onTerminate(this)
        NetworkManager.instance.unregisterReceiver(this)
    }

    fun initImageLoader() {
        val config = ImageLoaderConfig.Builder(LoaderEnum.GLIDE, GlideImageLoader())
                .maxMemory(40 * 1024 * 1024L)  // 单位为Byte
                .build()
        ImageLoader.init(this, config)
    }
//    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
//        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//            LogUtils.d(TAG, "onCreated: " + activity.componentName.className)
//        }
//
//        override fun onActivityStarted(activity: Activity) {
//        }
//
//        override fun onActivityResumed(activity: Activity) {
//
//        }
//
//        override fun onActivityPaused(activity: Activity) {
//
//        }
//
//        override fun onActivityStopped(activity: Activity) {
//
//        }
//
//
//        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
//
//        }
//
//        override fun onActivityDestroyed(activity: Activity) {
//            LogUtils.d(TAG, "onDestroy: " + activity.componentName.className)
//        }
//    }
}