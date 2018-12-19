package com.android.leo.toutiao

import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager
import com.android.leo.base.delegate.AppLifecycles
import com.android.leo.base.integration.ConfigModule

class TouTiaoConfiguration : ConfigModule {
    override fun injectAppLifecycle(context: Context?, lifecycles: MutableList<AppLifecycles>) {
        lifecycles.add(TouTiaoApp())
    }

    override fun injectActivityLifecycle(context: Context?, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>?) {
    }

    override fun injectFragmentLifecycle(context: Context?, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>?) {
    }
}