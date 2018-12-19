package com.android.leo.toutiao

import android.app.Application
import android.content.Context
import com.android.leo.base.delegate.AppLifecycles
import kotlin.properties.Delegates

class TouTiaoApp : AppLifecycles {
    companion object {

        private val TAG = "BaseApplication"

        var context: TouTiaoApp by Delegates.notNull()
            private set
    }

    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
        context = this
    }

    override fun onTerminate(application: Application) {
    }
}