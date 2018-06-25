package com.agile.android.leo.base

import android.os.Bundle
import com.agile.android.leo.integration.IActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * User: wanglg
 * Date: 2018-05-09
 * Time: 14:56
 * FIXME
 */
abstract class AgileActivity: RxAppCompatActivity(), IActivity {

//    public var mPresenter: P? = null//如果当前页面逻辑简单, Presenter 可以为 null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initPresenter()
        configViews()
        initData()
    }

   /* override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()
    }*/

}