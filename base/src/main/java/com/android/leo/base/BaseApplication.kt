package com.android.leo.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.agile.android.leo.utils.LogUtils
import com.android.leo.base.delegate.AppDelegate
import com.android.leo.base.delegate.AppLifecycles
import kotlin.properties.Delegates

class BaseApplication : Application() {

    private var mAppDelegate: AppLifecycles? = null
    companion object {

        private val TAG = "BaseApplication"

        var context: Context by Delegates.notNull()
            private set
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate==null){
            mAppDelegate=AppDelegate(base)
        }
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
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