package com.leowong.project.eyepetizer.ui.view.widgets

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import com.android.leo.base.events.NetChangeEvent
import com.android.leo.base.manager.NetworkManager
import com.android.leo.base.ui.dialog.IosAlertDialog
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leo.android.log.core.LogUtils
import com.leo.android.videoplayer.core.BaseVideoController
import com.leo.android.videoplayer.core.IMediaIntercept
import com.leo.android.videoplayer.core.IMediaPlayerControl
import com.leowong.project.eyepetizer.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import java.util.concurrent.TimeUnit

class VideoDetailMediaControlView : BaseVideoController, IMediaIntercept {


    var progress: ProgressBar? = null
    var seekbar: SeekBar? = null
    var videoCover: ImageView? = null
    var backImg: ImageView? = null
    var pauseOrPlay: ImageView? = null
    var lockScreen: ImageView? = null
    var fullscreen: ImageView? = null
    var controlView: View? = null
    var isPrepared: Boolean = false

    var coverPath: String? = null
    var title: String? = null
    var controlDisposable: Disposable? = null
    var mFormatBuilder: StringBuilder? = null
    var mFormatter: Formatter? = null
    var mVideoDuration: TextView? = null
    var mCurrentTime: TextView? = null
    var videoTitleTv: TextView? = null
    var netType: Int = 0
    var mobileNetTipDialog: IosAlertDialog? = null
    var skip: Boolean = false

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.item_video_detail_media_control, this)
        configViews()
    }

    override fun onFullScreenChange(isFullScreen: Boolean) {
        //记录控制器状态
        val visable = controlView?.visibility == View.VISIBLE
        removeAllViews()
        LayoutInflater.from(context).inflate(if (isFullScreen) {
            R.layout.item_video_detail_media_control_land
        } else {
            R.layout.item_video_detail_media_control_port
        }, this)
        configViews()
        if (visable) {
            showMediaControl()
        }
    }

    fun configViews() {
        mFormatBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        progress = findViewById(R.id.loading_progress)
        fullscreen = findViewById(R.id.fullscreen)
        pauseOrPlay = findViewById(R.id.pauseOrPlay)
        lockScreen = findViewById(R.id.lockScreen)
        videoTitleTv = findViewById(R.id.videoText)
        if (!TextUtils.isEmpty(title)) {
            videoTitleTv?.setText(title)
        }
        mCurrentTime = findViewById(R.id.time_current)
        mVideoDuration = findViewById(R.id.duration)
        seekbar = findViewById(R.id.mediacontroller_progress)
        videoCover = findViewById(R.id.vidoeCover)
        backImg = findViewById(R.id.media_player_back)
        controlView = findViewById(R.id.control_hierarchy)
        backImg?.setOnClickListener {
            if (videoControl != null && videoControl?.isFullScreen!!) {
                videoControl?.toggleFullScreen()
            } else {
                if (context != null) {
                    (context as Activity).finish()
                }
            }

        }
        pauseOrPlay?.setOnClickListener {
            if (videoControl != null) {
                if (videoControl?.isPlaying!!) {
                    videoControl?.pause()
                    pauseOrPlay?.setImageResource(R.mipmap.ic_player_play)
                    cancel()
                } else {
                    videoControl?.start()
                    showMediaControl()
                    pauseOrPlay?.setImageResource(R.mipmap.ic_player_pause)
                }
            }
        }
        isClickable = true
        setOnClickListener {
            if (controlView?.visibility == View.GONE) {
                showMediaControl()
            } else {
                controlView?.visibility = View.GONE
            }
        }
        fullscreen?.setOnClickListener {
            if (isPrepared) {
                videoControl?.toggleFullScreen()
            }
        }
        seekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                cancel()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                videoControl?.seekTo(seekBar.progress * videoControl?.duration!! / 100)
                showMediaControl()
            }

        })
        lockScreen?.setOnClickListener { it ->
            videoControl?.let {
                it.setLock(!it.lockState)
                lockScreen?.setImageResource(if (it.lockState) R.mipmap.ic_action_lock_outline else R.mipmap.ic_action_lock_open)
            }
        }
        videoControl?.let {
            if (it.isPlaying) {
                pauseOrPlay?.setImageResource(R.mipmap.ic_player_pause)
            } else {
                pauseOrPlay?.setImageResource(R.mipmap.ic_player_play)
            }
            if (isPrepared) {
                updatePlayDuration(it.currentPosition, it.duration)
            }
            if (it.lockState) {
                lockScreen?.setImageResource(R.mipmap.ic_action_lock_outline)
            } else {
                lockScreen?.setImageResource(R.mipmap.ic_action_lock_open)
            }
        }

    }

    override fun setMediaControl(player: IMediaPlayerControl) {
        super.setMediaControl(player)
        player.setMediaIntercept(this)
    }

    fun setVideoCover(cover: String) {
        coverPath = cover
        val coverOption = ImageLoaderOptions.Builder(videoCover!!, coverPath)
                .placeholder(R.drawable.placeholder_banner).isCrossFade(true).build()
        ImageLoader.showImage(coverOption)
    }

    fun setVideoTitle(title: String) {
        this.title = title
        videoTitleTv?.setText(title)
    }

    fun showMediaControl() {
        cancel()
        controlView?.visibility = View.VISIBLE
        controlDisposable = Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    controlView?.visibility = View.GONE
                })
    }

    fun cancel() {
        controlDisposable?.dispose()
        controlDisposable = null
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onCompletion() {
    }

    override fun onError(what: Int, extra: Int, msg: String?) {
    }

    override fun onFirstFrameStart() {
        progress?.visibility = View.GONE
        videoCover?.visibility = View.GONE
    }

    override fun onPrepared() {
        isPrepared = true
    }

    private fun stringForTime(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder?.setLength(0)
        return if (hours > 0) {
            mFormatter?.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter?.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    override fun updatePlayDuration(currentDuration: Long, videoDuration: Long) {
        seekbar?.setProgress((currentDuration * 100 / videoDuration).toInt())
        seekbar?.setSecondaryProgress((videoControl?.bufferPercentage!!))
        mVideoDuration?.setText(stringForTime(videoDuration))
        mCurrentTime?.setText(stringForTime(currentDuration))

    }

    override fun startPrepare(uri: Uri?) {
        isPrepared = false
        progress?.visibility = View.VISIBLE
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LogUtils.d("onAttachedToWindow")
        EventBus.getDefault().register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        LogUtils.d("onDetachedFromWindow")
        EventBus.getDefault().unregister(this)
    }

    override fun stopPlayer(isPlayComplete: Boolean) {
        isPrepared = false
        cancel()
    }

    override fun resetView() {
        cancelDialog()
    }

    //网络状态变化处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetChangeEvent(event: NetChangeEvent) {
        netType = NetworkManager.instance.networkType
        if (NetworkManager.instance.isMobileNetWorkConnected()) {
            cancelDialog()
            videoControl?.let {
                if (it.isPlaying) {
                    it.pause()
                    mobileNetTipDialog = IosAlertDialog(context).builder().setMessage(R.string.mobile_network_tip)
                            .setPositiveButton(R.string.mobile_network_play, object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    skip = true
                                    if (isPrepared) {
                                        it.start()
                                    } else {
                                        it.play()
                                    }
                                }
                            }).setNegativeButton(R.string.mobile_network_pause, null)
                    mobileNetTipDialog?.show()
                }
            }
        }
        LogUtils.w("onNetChangeEvent-->")
    }

    override fun onInfo(what: Int, extra: Int) {
    }

    override fun onLoadProgress(progress: Int) {
    }

    override fun onLoadStart() {
        progress?.visibility = View.VISIBLE
    }

    override fun onLoadEnd() {
        progress?.visibility = View.GONE
    }

    override fun interceptPlay(uri: Uri): Boolean {
        if (skip) {
            return false
        }
        if (NetworkManager.instance.isNetWorkConnected()) {
            if (NetworkManager.instance.isMobileNetWorkConnected()) {
                cancelDialog()
                mobileNetTipDialog = IosAlertDialog(context).builder().setMessage(R.string.mobile_network_tip)
                        .setPositiveButton(R.string.mobile_network_play, object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                skip = true
                                videoControl?.let {
                                    if (isPrepared) {
                                        it.start()
                                    } else {
                                        it.play()
                                    }
                                }
                            }
                        }).setNegativeButton(R.string.mobile_network_pause, null)
                mobileNetTipDialog?.show()
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }

    override fun interceptAttachView(): ViewGroup? {
        return null
    }

    fun cancelDialog() {
        mobileNetTipDialog?.dismiss()
    }
}