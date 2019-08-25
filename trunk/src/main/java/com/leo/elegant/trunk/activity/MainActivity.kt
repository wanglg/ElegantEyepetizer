package com.leo.elegant.trunk.activity

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import com.agile.android.leo.base.AgileFragment
import com.agile.android.leo.mvp.IPresenter
import com.android.leo.base.ui.activities.BaseActivity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.immersionbar.ImmersionBar
import com.leo.android.log.core.LogUtils
import com.leo.elegant.trunk.R
import com.leo.elegant.trunk.manager.TabManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<IPresenter>() {

    private var mIndex = 0

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun requestData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initPresenter() {
    }

    override fun configViews() {
        window?.setBackgroundDrawable(null)
        ImmersionBar.with(this).init()
        initTab()
        switchFragment(mIndex)
    }

    //初始化底部菜单
    private fun initTab() {

        //为Tab赋值
        tab_layout.setTabData(TabManager.getTabEntities())
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                //切换Fragment
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }
        })
    }

    fun switchFragment(position: Int) {
        if (position == 2) {
            LogUtils.w("position->3")
        }
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        val fragment = TabManager.getFragment(position)
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.container, fragment, fragment.javaClass.name)
        }

        mIndex = position
        tab_layout.currentTab = mIndex
        transaction.commitAllowingStateLoss()

    }

    override fun onDestroy() {
        super.onDestroy()
        TabManager.clearFragment()
    }

    override fun onBackPressed() {
        val currentFragment = TabManager.getFragment(mIndex)
        if (currentFragment is AgileFragment<*>) {
            if (currentFragment.onBack()) {
                return
            }
        }
        super.onBackPressed()
    }

    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        for (fragment in TabManager.getFragments()) {
            transaction.hide(fragment)
        }

    }


}
