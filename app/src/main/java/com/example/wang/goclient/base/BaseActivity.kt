package com.example.wang.goclient.base

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
abstract class BaseActivity<P : IPresenter> : AgileActivity() {
    open var mPresenter: P? = null//如果当前页面逻辑简单, Presenter 可以为 null
    open var mCompositeDisposable: CompositeDisposable? = null

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

    fun addDispose(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)//将所有 Disposable 放入集中处理
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

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    fun unDispose() {
        mCompositeDisposable?.clear()//保证 Activity 结束时取消所有正在执行的订阅
    }

    open fun useEventBus(): Boolean {
        return false
    }
}