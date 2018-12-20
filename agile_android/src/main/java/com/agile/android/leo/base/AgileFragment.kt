package com.agile.android.leo.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agile.android.leo.integration.IFragment
import com.agile.android.leo.mvp.IPresenter
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * User: wanglg
 * Date: 2018-05-10
 * Time: 20:24
 * FIXME
 */
abstract class AgileFragment<P : IPresenter> : RxFragment(), IFragment {

    open var fragmentParentView: View? = null
    private var isLastVisible = false
    private var hidden = false
    private var isFirst = true
    private var isResuming = false
    private var isViewDestroyed = false
    private var isLaz = false

    open var mCompositeDisposable: CompositeDisposable? = null
    open var mPresenter: P? = null//如果当前页面逻辑简单, Presenter 可以为 null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLastVisible = false
        hidden = false
        isFirst = true
        isViewDestroyed = false
        initData(arguments)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentParentView = inflater.inflate(getLayoutId(), container, false)
        return fragmentParentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
    }

    override fun onResume() {
        super.onResume()
        isResuming = true
        tryToChangeVisibility(true)
    }

    override fun onPause() {
        super.onPause()
        isResuming = false
        tryToChangeVisibility(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewDestroyed = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        setUserVisibleHintClient(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        onHiddenChangedClient(hidden)
    }

    private fun setUserVisibleHintClient(isVisibleToUser: Boolean) {
        tryToChangeVisibility(isVisibleToUser)
        if (isAdded) {
            // 当Fragment不可见时，其子Fragment也是不可见的。因此要通知子Fragment当前可见状态改变了。
            val fragments = childFragmentManager.fragments
            if (fragments != null) {
                for (fragment in fragments) {
                    if (fragment is AgileFragment<*>) {
                        fragment.setUserVisibleHintClient(isVisibleToUser)
                    }
                }
            }
        }
    }

    private fun onHiddenChangedClient(hidden: Boolean) {
        this.hidden = hidden
        tryToChangeVisibility(!hidden)
        if (isAdded) {
            val fragments = childFragmentManager.fragments
            if (fragments != null) {
                for (fragment in fragments) {
                    if (fragment is AgileFragment<*>) {
                        fragment.onHiddenChangedClient(hidden)
                    }
                }
            }
        }
    }

    private fun tryToChangeVisibility(tryToShow: Boolean) {
        // 上次可见
        if (isLastVisible) {
            if (tryToShow) {
                return
            }
            if (!isFragmentVisible()) {
                onFragmentPause()
                isLastVisible = false
            }
            // 上次不可见
        } else {
            val tryToHide = !tryToShow
            if (tryToHide) {
                return
            }
            if (isFragmentVisible()) {
                onFragmentResume(isFirst, isViewDestroyed)
                isLastVisible = true
                isFirst = false
            }
        }
    }

    /**
     * Fragment是否可见
     *
     * @return
     */
    fun isFragmentVisible(): Boolean {
        return if (isResuming()
                && userVisibleHint
                && !hidden) {
            true
        } else false
    }

    /**
     * Fragment 是否在前台。
     *
     * @return
     */
    private fun isResuming(): Boolean {
        return isResuming
    }

    /**
     * Fragment 可见时回调
     *
     * @param isFirst       是否是第一次显示
     * @param isViewDestroyed Fragment 的 View 被回收，但是Fragment实例仍在。
     */
    open fun onFragmentResume(isFirst: Boolean, isViewDestroyed: Boolean) {
        //TODO 重写此方法需要 调用onFragmentResume
        if (isFirst) {
            requestData()
        }
    }

    /**
     * Fragment 不可见时回调
     */
    open fun onFragmentPause() {

    }


    open fun <T : View> findViewById(@IdRes id: Int): T? {
        return fragmentParentView?.findViewById(id)
    }

    override fun onBack(): Boolean {
        return false
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
        mCompositeDisposable = null
        mPresenter?.onDestroy()
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    fun unDispose() {
        mCompositeDisposable?.clear()//保证 Activity 结束时取消所有正在执行的订阅
    }
}