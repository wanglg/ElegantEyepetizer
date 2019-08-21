package com.shortvideo.android.leo.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import android.widget.FrameLayout
import com.agile.android.leo.exception.ApiException
import com.android.leo.base.GlobalConstant
import com.android.leo.base.ui.fragments.BaseFragment
import com.chad.library.adapter.base.BaseViewHolder
import com.leo.android.log.core.LogUtils
import com.leo.android.videoplayer.IjkVideoView
import com.leo.android.videoplayer.PlayerAttachListManager
import com.leo.android.videoplayer.SimpleMediaPlayerListener
import com.leo.android.videoplayer.ijk.PlayerConfig
import com.leo.android.videoplayer.utils.VideoUrlUtils
import com.leowang.shortvideo.R
import com.sankuai.waimai.router.annotation.RouterService
import com.shortvideo.android.leo.mvp.contract.SmallVideoContract
import com.shortvideo.android.leo.mvp.model.SmallVideoModel
import com.shortvideo.android.leo.mvp.model.entity.VideoBean
import com.shortvideo.android.leo.mvp.presenter.ShortVideoPresenter
import com.shortvideo.android.leo.ui.adapters.ShortVideoTabAdapter
import com.shortvideo.android.leo.ui.view.ShortVideoControlView
import com.shortvideo.android.leo.ui.view.widgets.OnPageStateChangedListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_small_video_tab.*
import java.util.concurrent.TimeUnit

@RouterService(interfaces = arrayOf(Fragment::class), key = arrayOf(GlobalConstant.Fragment.LITTLE_VIDEO))
class SmallVideoTabFragment : BaseFragment<ShortVideoPresenter>(), SmallVideoContract.View, OnPageStateChangedListener {

    private val playerListManager by lazy {
        PlayerAttachListManager.Builder(activity!!).playerConfig(PlayerConfig.Builder().setLooping().enableCache().calculateMatch().build())
                .preLoad(true).build()
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val shortVideoTabAdapter by lazy {
        ShortVideoTabAdapter(ArrayList())
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_small_video_tab;
    }

    override fun initPresenter() {
        mPresenter = ShortVideoPresenter(SmallVideoModel(), this)
    }

    override fun configViews() {
        mRefreshLayout.setOnRefreshListener({

        })
        recycler_pager_view.layoutManager = linearLayoutManager
        recycler_pager_view.adapter = shortVideoTabAdapter
        recycler_pager_view.addOnPageChangedListener(this)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun requestData() {
        mPresenter?.requestVideoData()
    }

    override fun setVideoData(data: ArrayList<VideoBean>) {
        shortVideoTabAdapter.replaceData(data)
        addDispose(Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    changToPosition(0)
                }))
    }

    fun changToPosition(position: Int) {
        val baseViewHolder = recycler_pager_view.findViewHolderForAdapterPosition(position) as BaseViewHolder
        val videoBean = shortVideoTabAdapter.getItem(position)
        playerListManager.release()
        playerListManager.attachView(baseViewHolder.getView(R.id.video_parent) as ViewGroup)
        playerListManager.addMediaPlayerListener(object : SimpleMediaPlayerListener() {
            override fun onFirstFrameStart() {
                LogUtils.d("onFirstFrameStart")
                baseViewHolder.setGone(R.id.videoCover, false)
            }

            override fun stopPlayer(isPlayComplete: Boolean) {
                LogUtils.d("stopPlayer")
                baseViewHolder.setGone(R.id.videoCover, true)
            }
        })
        if (position >= 0 && position < shortVideoTabAdapter.itemCount - 1) {
            val nextVideoBean = shortVideoTabAdapter.getItem(position + 1)
            playerListManager.nextPath = VideoUrlUtils.convertRemoteUrl(nextVideoBean!!.url)
        }
        val nextViewHolder = recycler_pager_view.findViewHolderForAdapterPosition(position + 1)
        LogUtils.d("wang", "nextViewHolder->" + (nextViewHolder == null))
        playerListManager.attachMediaControl(ShortVideoControlView(activity!!))
        playerListManager.currentPath = VideoUrlUtils.convertRemoteUrl(videoBean!!.url)
        playerListManager.go()
    }

    fun havePlayer(position: Int): Boolean {
        val baseViewHolder = recycler_pager_view.findViewHolderForAdapterPosition(position)
        if (baseViewHolder == null) {
            return false
        } else {
            val viewGroup = baseViewHolder.itemView.findViewById<FrameLayout>(R.id.video_parent)
            for (index in 0..viewGroup.childCount - 1) {
                if (viewGroup.getChildAt(index) is IjkVideoView) {
                    return true
                }
            }
            return false
        }
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
        playerListManager.onPause()
    }

    override fun onFragmentResume(isFirst: Boolean, isViewDestroyed: Boolean) {
        super.onFragmentResume(isFirst, isViewDestroyed)
        playerListManager.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        playerListManager.release()
    }

    override fun resultError(exception: ApiException) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun onPageChanged(oldPosition: Int, newPosition: Int) {
        if (oldPosition != newPosition) {
            changToPosition(newPosition)
        } else {
            if (!havePlayer(newPosition)) {
                changToPosition(newPosition)
            }
        }
    }

    override fun onFlingToOtherPosition() {
        playerListManager.release()
    }

    override fun onPageDetachedFromWindow(position: Int) {
        if (havePlayer(position)) {
            playerListManager.release()
        }
    }
}