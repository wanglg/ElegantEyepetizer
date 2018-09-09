package com.leowong.project.eyepetizer.ui.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import com.agile.android.leo.mvp.IPresenter
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.BaseActivity
import com.leowong.project.eyepetizer.showToast
import com.leowong.project.eyepetizer.ui.adapters.VideoRecommendAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class HomeActivity : BaseActivity<IPresenter>() {
    var adapter: VideoRecommendAdapter? = null
    var scrollState: Boolean = true
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
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = VideoRecommendAdapter(R.layout.item_video_recommend, getlist())
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        recyclerView.smoothScrollToPosition(4)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && scrollState) {
                    scrollState = false
                    recyclerView?.smoothScrollToPosition(0)
                }
            }
        })
//        Observable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe({
//            recyclerView.smoothScrollToPosition(4)
//            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE && scrollState) {
//                        scrollState = false
//                        manager.setSpeedFast()
//                        recyclerView?.smoothScrollToPosition(0)
//                    }
//                }
//            })
//        })

//        recyclerView.scrollBy(-dip2px(this, 800f),0)
//        recyclerView.smoothScrollBy()
//        val moveToLeftAnim = ObjectAnimator.ofFloat(recyclerView, "translationX", -dip2px(this, 800f).toFloat())
//        moveToLeftAnim.duration = 1500
////        moveToLeftAnim?.repeatMode = ValueAnimator.REVERSE;
//        moveToLeftAnim.start()
//        Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe({
//
//        })
    }

    private fun getlist(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Item_1")
        list.add("Item_2")
        list.add("Item_3")
        list.add("Item_4")
        list.add("Item_5")
        list.add("Item_6")
        list.add("Item_7")
        list.add("Item_8")
        return list
    }

    private var mExitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                showToast("再按一次退出程序")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
