package com.agile.android.leo.mvp

import android.app.Activity
import com.agile.android.leo.utils.Preconditions
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * User: wanglg
 * Date: 2018-05-02
 * Time: 15:01
 * FIXME
 */
open class BasePresenter<M : IModel, V : IView> : IPresenter {

    protected var mCompositeDisposable: CompositeDisposable? = null
    protected var mModel: M? = null
    protected var mRootView: V? = null

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     *
     * @param model
     * @param rootView
     */
    public constructor(model: M, rootView: V) {
        Preconditions.checkNotNull(model, "%s cannot be null", IModel::class.java.name)
        Preconditions.checkNotNull(rootView, "%s cannot be null", IView::class.java.name)
        this.mModel = model
        this.mRootView = rootView
        this.mCompositeDisposable = CompositeDisposable()
        onStart()
    }

    constructor() {
        onStart()
    }

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     *
     * @param model
     * @param rootView
     */
    constructor(rootView: V) {
        Preconditions.checkNotNull(rootView, "%s cannot be null", IView::class.java.name)
        this.mRootView = rootView
        onStart()
    }

    override final fun onStart() {
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        unDispose()
        mModel?.onDestroy()
        mRootView = null
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        this.mCompositeDisposable = null
    }

    /**
     * 是否使用 [EventBus],默认为使用(false)，
     *
     * @return
     */
    fun useEventBus(): Boolean {
        return false
    }

    /**
     * 将 [Disposable] 添加到 [CompositeDisposable] 中统一管理
     * 可在 [Activity.onDestroy] 中使用 [.unDispose] 停止正在执行的 RxJava 任务,避免内存泄漏
     * 目前框架已使用 [RxLifecycle] 避免内存泄漏,此方法作为备用方案
     *
     * @param disposable
     */
    fun addDispose(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)//将所有 Disposable 放入集中处理
    }

    fun removeDispose(disposable: Disposable) {
        mCompositeDisposable?.remove(disposable)
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    fun unDispose() {
        mCompositeDisposable?.clear()//保证 Activity 结束时取消所有正在执行的订阅
    }
}