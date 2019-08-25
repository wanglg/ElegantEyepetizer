package com.android.leo.toutiao.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.LinearLayout
import com.agile.android.leo.base.AgileFragment
import com.agile.android.leo.mvp.IPresenter
import com.android.leo.base.GlobalConstant
import com.android.leo.base.ui.fragments.BaseFragment
import com.android.leo.toutiao.Constant
import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.model.TouTiaoModel
import com.android.leo.toutiao.ui.adapter.ChannelPagerAdapter
import com.gyf.immersionbar.ImmersionBar
import com.leo.android.log.core.LogUtils
import com.sankuai.waimai.router.annotation.RouterService
import kotlinx.android.synthetic.main.fragment_toutiao.*
import java.util.*

@RouterService(interfaces = arrayOf(Fragment::class), key = arrayOf(GlobalConstant.Fragment.TOUTIAO))
class TouTiaoFragment : BaseFragment<IPresenter>() {
    private val mModel by lazy {
        TouTiaoModel()
    }
    private val mSelectedChannels by lazy {
        mModel.getSelectChannels()
    }
    private val mChannelCodes by lazy {
        resources.getStringArray(R.array.channel_code)
    }
    private var mChannelPagerAdapter: ChannelPagerAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_toutiao
    }

    private val mChannelFragments = ArrayList<NewsListFragment>()
    override fun initPresenter() {
    }

    override fun configViews() {
        LogUtils.w("TouTiaoFragment configViews")
        val lp = status_bar_view.layoutParams as LinearLayout.LayoutParams
        lp.height = ImmersionBar.getStatusBarHeight(activity!!)
        status_bar_view.layoutParams = lp
        mChannelPagerAdapter = ChannelPagerAdapter(mChannelFragments, mSelectedChannels, getChildFragmentManager())
        vp_content.setAdapter(mChannelPagerAdapter)
        vp_content.setOffscreenPageLimit(mSelectedChannels.size)
        tab_channel.setViewPager(vp_content)
    }

    private fun initChannelFragments() {
        LogUtils.w("TouTiaoFragment initChannelFragments start")
        for (channel in mSelectedChannels) {
            val newsFragment = NewsListFragment()
            val bundle = Bundle()
            bundle.putString(Constant.CHANNEL_CODE, channel.channelCode)
            bundle.putBoolean(Constant.IS_VIDEO_LIST, channel.channelCode.equals(mChannelCodes[1]))//是否是视频列表页面,根据判断频道号是否是视频
            newsFragment.arguments = bundle
            mChannelFragments.add(newsFragment)//添加到集合中
        }
        LogUtils.w("TouTiaoFragment initChannelFragments end")
    }

    override fun initData(savedInstanceState: Bundle?) {
        initChannelFragments()
    }

    override fun onBack(): Boolean {
        if (mChannelPagerAdapter != null) {
            val fragment = mChannelPagerAdapter!!.getItem(vp_content.currentItem)
            if (fragment is AgileFragment<*>) {
                return fragment.onBack()
            }
        }
        return false
    }

    override fun requestData() {
    }
}