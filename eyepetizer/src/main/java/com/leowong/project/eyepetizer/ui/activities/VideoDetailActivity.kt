package com.leowong.project.eyepetizer.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.agile.android.leo.exception.ApiException
import com.android.leo.base.getScreenWidth
import com.android.leo.base.showToast
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.gyf.immersionbar.ImmersionBar
import com.leo.android.log.core.LogUtils
import com.leo.android.videoplayer.SimpleMediaPlayerListener
import com.leo.android.videoplayer.ijk.PlayerConfig
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.AppBaseActivity
import com.leowong.project.eyepetizer.events.VideoDetailItemClickEvent
import com.leowong.project.eyepetizer.mvp.contract.VideoDetailContract
import com.leowong.project.eyepetizer.mvp.model.VideoDetailModel
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.mvp.presenter.VideoDetailPresenter
import com.leowong.project.eyepetizer.ui.adapters.VideoDetailAdapter
import com.leowong.project.eyepetizer.ui.view.widgets.VideoDetailMediaControlView
import kotlinx.android.synthetic.main.activity_video_detail.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class VideoDetailActivity : AppBaseActivity<VideoDetailPresenter>(), VideoDetailContract.View {
    protected var videoDetailAdapter: VideoDetailAdapter? = null
    protected var videoDetailMediaControlView: VideoDetailMediaControlView? = null
    protected var footView: View? = null

    private var playPosition = 0
    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun setVideo(url: String) {
        LogUtils.d("wang", "setVideo")
        ijkvideo.release()
        ijkvideo.setVideoPath(url)
        initVideoControl()
        ijkvideo.addMediaPlayerListener(object : SimpleMediaPlayerListener() {
            override fun onFirstFrameStart() {
                super.onFirstFrameStart()
                LogUtils.d("wang", "onFirstFrameStart")
            }
        })
        ijkvideo.play()
    }

    override fun setVideoInfo(itemInfo: HomeBean.Issue.Item) {

        multipleStatusView?.showContent()
        videoDetailAdapter?.addItemData(itemInfo)

        // 请求相关的最新等视频
        mPresenter?.requestRelatedVideo(itemInfo.data?.id ?: 0)


    }

    override fun setBackground(url: String) {
        Glide.with(this).load(url).listener(GlidePalette.with(url)
                .use(BitmapPalette.Profile.MUTED_DARK).crossfade(true).intoBackground(findViewById(R.id.status_bar_view))).into(mVideoBackground)
    }

    override fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {
        videoDetailAdapter?.addItemData(itemList)
    }

    override fun setErrorMsg(errorMsg: String) {
    }

    override fun resultError(exception: ApiException) {
        showToast(exception.message!!)
    }

    override fun showLoading() {
        footView = View.inflate(this, R.layout.item_video_detail_loading, null)
        videoDetailAdapter?.setFooterView(footView)
    }

    override fun dismissLoading() {
        videoDetailAdapter?.removeFooterView(footView)
    }

    /**
     * Item 详细数据
     */
    private lateinit var itemData: HomeBean.Issue.Item

    companion object {
        val BUNDLE_VIDEO_DATA = "video_data"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_video_detail
    }

    override fun initPresenter() {
        mPresenter = VideoDetailPresenter(VideoDetailModel(), this)
    }

    override fun onPause() {
        super.onPause()
        ijkvideo.onPause()
    }

    override fun onBackPressed() {
        ijkvideo?.let {
            if (it.isFullScreen) {
                it.toggleFullScreen()
                return
            }
        }
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        ijkvideo.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        ijkvideo.onDestory()
    }

    override fun configViews() {
        ImmersionBar.with(this).init()
        wrapStatusBarView(findViewById<View>(android.R.id.content), Color.BLACK)
        initSlide()
        val layoutParams = mVideoView.getLayoutParams()
        //按照16：9设置播放器视图
        layoutParams.height = getScreenWidth(this) * 9 / 16
        mVideoView.setLayoutParams(layoutParams)
        multipleStatusView = videoDetailMultipleStatusView
        mRecyclerView.layoutManager = linearLayoutManager
        videoDetailAdapter = VideoDetailAdapter(ArrayList())
        mRecyclerView.adapter = videoDetailAdapter
        ijkvideo?.setPlayerConfig(PlayerConfig.Builder().autoRotate().build())
    }

    fun wrapStatusBarView(view: View?, statusBarColor: Int) {
        if (view == null) {
            return
        }
        val parent = view.parent as ViewGroup
        val linearLayout = LinearLayout(view.context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val statusBarView = View(view.context)
        statusBarView.id = R.id.status_bar_view
        statusBarView.setBackgroundColor(statusBarColor)
        linearLayout.addView(statusBarView, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ImmersionBar.getStatusBarHeight(this)))
        parent.removeView(view)
        linearLayout.addView(view)
        parent.addView(linearLayout, -1, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    fun initVideoControl() {
        videoDetailMediaControlView = VideoDetailMediaControlView(this)
        videoDetailMediaControlView?.setMediaControl(ijkvideo)
        val cover = itemData.data?.cover?.feed
        cover?.let {
            videoDetailMediaControlView?.setVideoCover(it)
        }
        videoDetailMediaControlView?.setVideoTitle(itemData.data!!.title)
    }

    override fun initData(savedInstanceState: Bundle?) {
        itemData = intent.getSerializableExtra(BUNDLE_VIDEO_DATA) as HomeBean.Issue.Item
    }

    override fun requestData() {
        mPresenter?.loadVideoInfo(itemData)
    }


    override fun useEventBus(): Boolean {
        return true
    }

    //网络状态变化处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onVideoDetailItemClickEvent(event: VideoDetailItemClickEvent) {
        itemData = event.itemInfo
        multipleStatusView?.showLoading()
        mPresenter?.loadVideoInfo(event.itemInfo)
    }
}