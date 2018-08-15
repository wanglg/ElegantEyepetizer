package com.agile.android.leo.base

import android.os.Bundle
import com.agile.android.leo.integration.IActivity
import com.agile.android.leo.mvp.IPresenter
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * User: wanglg
 * Date: 2018-05-09
 * Time: 14:56
 * FIXME
 */
abstract class AgileActivity<P : IPresenter> : RxAppCompatActivity(), IActivity {
    open var mPresenter: P? = null//如果当前页面逻辑简单, Presenter 可以为 null
    open var mCompositeDisposable: CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
        setContentView(getLayoutId())
        initPresenter()
        configViews()
        requestData()

    }

    fun addDispose(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)//将所有 Disposable 放入集中处理
    }

    override fun onDestroy() {
        super.onDestroy()
        unDispose()
        mPresenter?.onDestroy()
        mCompositeDisposable = null
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    fun unDispose() {
        mCompositeDisposable?.clear()//保证 Activity 结束时取消所有正在执行的订阅
    }

}