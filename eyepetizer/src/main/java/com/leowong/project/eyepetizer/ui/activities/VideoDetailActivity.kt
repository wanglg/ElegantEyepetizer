package com.leowong.project.eyepetizer.ui.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.agile.android.leo.exception.ApiException
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leo.android.videplayer.ijk.PlayerConfig
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.base.AppBaseActivity
import com.leowong.project.eyepetizer.events.VideoDetailItemClickEvent
import com.leowong.project.eyepetizer.mvp.contract.VideoDetailContract
import com.leowong.project.eyepetizer.mvp.model.VideoDetailModel
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.mvp.presenter.VideoDetailPresenter
import com.android.leo.base.showToast
import com.leowong.project.eyepetizer.ui.adapters.VideoDetailAdapter
import com.leowong.project.eyepetizer.ui.view.widgets.VideoDetailMediaControlView
import com.android.leo.base.utils.StatusBarUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_video_detail.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

class VideoDetailActivity : AppBaseActivity<VideoDetailPresenter>(), VideoDetailContract.View {
    protected var videoDetailAdapter: VideoDetailAdapter? = null
    protected var videoDetailMediaControlView: VideoDetailMediaControlView? = null
    protected var footView: View? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun setVideo(url: String) {
        ijkvideo.setVideoPath(url)
        ijkvideo.startPlay()
        initVideoControl()
    }

    override fun setVideoInfo(itemInfo: HomeBean.Issue.Item) {

        multipleStatusView?.showContent()
        videoDetailAdapter?.addItemData(itemInfo)

        // 请求相关的最新等视频
        mPresenter?.requestRelatedVideo(itemInfo.data?.id ?: 0)


    }

    override fun setBackground(url: String) {
        val imageLoaderOptions = ImageLoaderOptions.Builder(mVideoBackground, url).isCrossFade(true).build()
        ImageLoader.showImage(imageLoaderOptions)
    }

    override fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {
        videoDetailAdapter?.addItemData(itemList)
        addDispose(Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    videoDetailAdapter?.setFooterView(View.inflate(this, R.layout.item_footer, null))
                }))
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
        StatusBarUtils.with(this).init()
        initSlide()
        multipleStatusView = videoDetailMultipleStatusView
        mRecyclerView.layoutManager = linearLayoutManager
        videoDetailAdapter = VideoDetailAdapter(ArrayList())
        mRecyclerView.adapter = videoDetailAdapter
        ijkvideo?.setPlayerConfig(PlayerConfig.Builder().autoRotate().build())
    }

    fun initVideoControl() {
        videoDetailMediaControlView = VideoDetailMediaControlView(this)
        videoDetailMediaControlView?.setMediaControl(ijkvideo)
        val cover = itemData.data?.cover?.feed
        cover?.let {
            videoDetailMediaControlView?.setVideoCover(it)
        }
        videoDetailMediaControlView?.setVideoTitle(itemData.data!!.title)
//        ijkvideo?.addMediaPlayerListener(videoDetailMediaControlView!!)
//        ijkvideo?.attachMediaControl(videoDetailMediaControlView!!)
    }

    override fun initData(savedInstanceState: Bundle?) {
        itemData = intent.getSerializableExtra(BUNDLE_VIDEO_DATA) as HomeBean.Issue.Item
//        // init player
//        IjkMediaPlayer.loadLibrariesOnce(null)
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
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