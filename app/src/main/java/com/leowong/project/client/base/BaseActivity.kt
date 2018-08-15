package com.leowong.project.client.base

import android.annotation.SuppressLint
import android.os.Bundle
import com.agile.android.leo.base.AgileActivity
import com.agile.android.leo.mvp.IPresenter
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()
        unDispose()
        mCompositeDisposable = null
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
}