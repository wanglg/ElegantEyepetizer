package com.agile.android.leo.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agile.android.leo.integration.IFragment
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * User: wanglg
 * Date: 2018-05-10
 * Time: 20:24
 * FIXME
 */
abstract class AgileFragment : RxFragment(), IFragment {
    open var fragmentParentView: View? = null
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
        initData()
    }

    open fun <T : View> findViewById(@IdRes id: Int): T? {
        return fragmentParentView?.findViewById(id)
    }
}