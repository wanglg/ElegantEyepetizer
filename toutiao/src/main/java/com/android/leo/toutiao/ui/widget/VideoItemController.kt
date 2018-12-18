package com.android.leo.toutiao.ui.widget

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.model.entity.News
import com.leo.android.videplayer.core.BaseVideoController
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class VideoItemController : BaseVideoController {
    var news: News? = null
    var progress: ProgressBar? = null
    var seekbar: SeekBar? = null
    var videoCover: ImageView? = null
    var backImg: ImageView? = null
    var pauseOrPlay: ImageView? = null
    var fullscreen: ImageView? = null
    var controlView: View? = null
    var isPrepared: Boolean = false
    var controlDisposable: Disposable? = null
    var mFormatBuilder: StringBuilder? = null
    var mFormatter: Formatter? = null
    var mVideoDuration: TextView? = null
    var mCurrentTime: TextView? = null
    var videoTitleTv: TextView? = null

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.item_video_feed_media_control_port, this)
        configViews()
    }

    override fun resetView() {
    }

    fun configViews() {
        mFormatBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        progress = findViewById(R.id.loading_progress)
        fullscreen = findViewById(R.id.fullscreen)
        pauseOrPlay = findViewById(R.id.pauseOrPlay)
        videoTitleTv = findViewById(R.id.videoText)
//        if (!TextUtils.isEmpty(title)) {
//            videoTitleTv?.setText(title)
//        }
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
                    pauseOrPlay?.setImageResource(R.mipmap.ic_toutiao_play_start)
                    cancel()
                } else {
                    videoControl?.start()
                    showMediaControl()
                    pauseOrPlay?.setImageResource(R.mipmap.ic_toutiao_play_pause)
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

        videoControl?.let {
            if (it.isPlaying) {
                pauseOrPlay?.setImageResource(R.mipmap.ic_toutiao_play_pause)
            } else {
                pauseOrPlay?.setImageResource(R.mipmap.ic_toutiao_play_start)
            }
            if (isPrepared) {
                updatePlayDuration(it.currentPosition, it.duration)
            }

        }
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

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onCompletion() {
    }

    override fun onError(what: Int, extra: Int, msg: String?) {
    }

    override fun onFirstFrameStart() {
    }

    override fun onPrepared() {
    }

    override fun updatePlayDuration(currentDuration: Long, videoDuration: Long) {
    }

    override fun startPrepare(uri: Uri?) {
    }

    override fun stopPlayer(isPlayComplete: Boolean) {
    }

    override fun onInfo(what: Int, extra: Int) {
    }

    fun cancel() {
        controlDisposable?.dispose()
        controlDisposable = null
    }

    override fun onLoadProgress(progress: Int) {
    }

    override fun onLoadStart() {
    }

    override fun onLoadEnd() {
    }

    override fun onFullScreenChange(isFullScreen: Boolean) {
    }
}