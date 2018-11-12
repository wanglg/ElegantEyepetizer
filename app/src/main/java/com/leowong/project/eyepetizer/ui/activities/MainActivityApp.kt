package com.leowong.project.eyepetizer.ui.activities

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import com.agile.android.leo.mvp.IPresenter
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.AppBaseActivity
import com.leowong.project.eyepetizer.entity.TabEntity
import com.leowong.project.eyepetizer.ui.fragments.HomeFragment
import com.leowong.project.eyepetizer.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivityApp : AppBaseActivity<IPresenter>() {

    private val mTitles = arrayOf("每日精选", "发现", "热门", "我的")
    // 未被选中的图标
    private val mIconUnSelectIds = intArrayOf(R.mipmap.ic_home_normal, R.mipmap.ic_discovery_normal, R.mipmap.ic_hot_normal, R.mipmap.ic_mine_normal)
    // 被选中的图标
    private val mIconSelectIds = intArrayOf(R.mipmap.ic_home_selected, R.mipmap.ic_discovery_selected, R.mipmap.ic_hot_selected, R.mipmap.ic_mine_selected)

    private val mTabEntities = ArrayList<CustomTabEntity>()

    private var mHomeFragment: HomeFragment? = null
    private var mDiscoveryFragment: HomeFragment? = null
    private var mHotFragment: HomeFragment? = null
    private var mMineFragment: HomeFragment? = null
    //默认为0
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
        StatusBarUtils.with(this).init()
        initTab()
        switchFragment(mIndex)
    }

    //初始化底部菜单
    private fun initTab() {
        (0 until mTitles.size)
                .mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        //为Tab赋值
        tab_layout.setTabData(mTabEntities)
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
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (position) {
            0 // 首页
            -> mHomeFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                mHomeFragment = it
                transaction.add(R.id.container, it, "home")
            }
            1  //发现
            -> mDiscoveryFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                mDiscoveryFragment = it
                transaction.add(R.id.container, it, "discovery")
            }
            2  //热门
            -> mHotFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                mHotFragment = it
                transaction.add(R.id.container, it, "hot")
            }
            3 //我的
            -> mMineFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                mMineFragment = it
                transaction.add(R.id.container, it, "mine")
            }

            else -> {

            }
        }

        mIndex = position
        tab_layout.currentTab = mIndex
        transaction.commitAllowingStateLoss()

    }

    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mDiscoveryFragment?.let { transaction.hide(it) }
        mHotFragment?.let { transaction.hide(it) }
        mMineFragment?.let { transaction.hide(it) }
    }


}
