package com.android.leo.base.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.agile.android.leo.base.AgileFragment
import com.agile.android.leo.mvp.IPresenter
import com.classic.common.MultipleStatusView
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment<P : IPresenter> : AgileFragment<P>() {
    var multipleStatusView: MultipleStatusView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    open fun useEventBus(): Boolean {
        return false
    }

    fun startActivity(activity: Class<out Activity>) {
        startActivity(activity, null)
    }

    fun startActivity(activity: Class<out Activity>, bundle: Bundle?) {
        val intent = Intent(getActivity(), activity)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }
}