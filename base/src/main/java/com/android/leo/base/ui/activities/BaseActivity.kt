package com.android.leo.base.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.agile.android.leo.base.AgileActivity
import com.agile.android.leo.mvp.IPresenter
import com.classic.common.MultipleStatusView
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

/**
 * User: wanglg
 * Date: 2018-05-11
 * Time: 11:21
 * FIXME
 */
@SuppressLint("Registered")
abstract class BaseActivity<P : IPresenter> : AgileActivity<P>() {
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


    fun <T> getTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .compose(bindToLifecycle())
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle?) {
        //No call for super(). Bug on API Level > 11.会导致fragment重叠问题
        //https://stackoverflow.com/questions/7575921/illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-wit
        //super.onSaveInstanceState(outState)
    }


    open fun useEventBus(): Boolean {
        return false
    }

    fun startActivity(activity: Class<out Activity>) {
        startActivity(activity, null)
    }

    fun startActivity(activity: Class<out Activity>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }


}