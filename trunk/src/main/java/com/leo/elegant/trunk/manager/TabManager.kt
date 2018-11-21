package com.leo.elegant.trunk.manager

import android.support.v4.app.Fragment
import android.util.SparseArray
import com.android.leo.base.Constant
import com.flyco.tablayout.listener.CustomTabEntity
import com.leo.elegant.trunk.R
import com.leo.elegant.trunk.entity.TabEntity
import com.sankuai.waimai.router.Router
import java.util.*

object TabManager {
    private val mTitles = arrayOf("开眼", "小视频", "头条", "我的")
    // 未被选中的图标
    private val mIconUnSelectIds = intArrayOf(R.mipmap.ic_home_normal, R.mipmap.ic_discovery_normal, R.mipmap.ic_hot_normal, R.mipmap.ic_mine_normal)
    // 被选中的图标
    private val mIconSelectIds = intArrayOf(R.mipmap.ic_home_selected, R.mipmap.ic_discovery_selected, R.mipmap.ic_hot_selected, R.mipmap.ic_mine_selected)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mFragments = ArrayList<Fragment>()
    private val mFragmentsArray = SparseArray<Fragment>()

    init {
        (0 until mTitles.size)
                .mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
    }

    public fun getTabEntities(): ArrayList<CustomTabEntity> {
        return mTabEntities
    }

    public fun getFragment(position: Int): Fragment {
        var fragment = mFragmentsArray.get(position)
        if (fragment != null) {
            return fragment
        } else {
            when (position) {
                0 -> {
                    fragment = Router.getServiceClass(Fragment::class.java,
                            Constant.Fragment.EYEPETIZER).newInstance()
                    mFragmentsArray.append(position, fragment)
                    mFragments.add(fragment)
                }
                1 -> {
                    fragment = Router.getServiceClass(Fragment::class.java,
                            Constant.Fragment.EYEPETIZER).newInstance()
                    mFragmentsArray.append(position, fragment)
                    mFragments.add(fragment)
                }
                2 -> {
                    fragment = Router.getServiceClass(Fragment::class.java,
                            Constant.Fragment.EYEPETIZER).newInstance()
                    mFragmentsArray.append(position, fragment)
                    mFragments.add(fragment)
                }
                3 -> {
                    fragment = Router.getServiceClass(Fragment::class.java,
                            Constant.Fragment.EYEPETIZER).newInstance()
                    mFragmentsArray.append(position, fragment)
                    mFragments.add(fragment)
                }
                else -> {
                    fragment = Router.getServiceClass(Fragment::class.java,
                            Constant.Fragment.EYEPETIZER).newInstance()
                    mFragmentsArray.append(position, fragment)
                    mFragments.add(fragment)
                }
            }
            return fragment
        }

    }

    public fun getFragments(): ArrayList<Fragment> {
        return mFragments
    }
}