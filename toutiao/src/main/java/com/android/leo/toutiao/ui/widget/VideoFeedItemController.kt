package com.android.leo.toutiao.ui.widget

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
import com.leo.android.videoplayer.core.BaseVideoController
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class VideoFeedItemController : BaseVideoController {
    var news: News? = null
    var progress: ProgressBar? = null
    var playProgress: ProgressBar? = null
    var seekbar: SeekBar? = null
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
        playProgress = findViewById(R.id.play_progress)
        playProgress?.visibility = View.VISIBLE
        fullscreen = findViewById(R.id.fullscreen)
        pauseOrPlay = findViewById(R.id.pauseOrPlay)
        videoTitleTv = findViewById(R.id.videoText)
        news?.let {
            videoTitleTv?.setText(it.title)
        }
        mCurrentTime = findViewById(R.id.time_current)
        mVideoDuration = findViewById(R.id.duration)
        seekbar = findViewById(R.id.mediacontroller_progress)
        controlView = findViewById(R.id.control_hierarchy)
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
                playProgress?.visibility = View.VISIBLE
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
                playProgress?.setProgress((it.currentPosition * 100 / it.duration).toInt())
            }

        }
    }

    fun showMediaControl() {
        cancel()
        controlView?.visibility = View.VISIBLE
        playProgress?.visibility = View.GONE
        controlDisposable = Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    controlView?.visibility = View.GONE
                    playProgress?.visibility = View.VISIBLE
                })
    }

    fun setNew(news: News) {
        this.news = news
        videoTitleTv?.setText(news.title)
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onCompletion() {
    }

    override fun onError(what: Int, extra: Int, msg: String?) {
    }

    override fun onFirstFrameStart() {
        progress?.visibility = View.GONE
    }

    override fun onPrepared() {
        isPrepared = true
    }

    override fun updatePlayDuration(currentDuration: Long, videoDuration: Long) {
        val progress = (currentDuration * 100 / videoDuration).toInt()
        seekbar?.setProgress(progress)
        playProgress?.setProgress(progress)
        seekbar?.setSecondaryProgress((videoControl?.bufferPercentage!!))
        mVideoDuration?.setText(stringForTime(videoDuration))
        mCurrentTime?.setText(stringForTime(currentDuration))
    }

    override fun startPrepare(uri: Uri?) {
        isPrepared = false
        progress?.visibility = View.VISIBLE
    }

    override fun stopPlayer(isPlayComplete: Boolean) {
        isPrepared = false
        cancel()
        videoControl?.detachMediaControl()
    }

    override fun onInfo(what: Int, extra: Int) {
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

    fun cancel() {
        controlDisposable?.dispose()
        controlDisposable = null
    }

    override fun onLoadProgress(progress: Int) {
    }

    override fun onLoadStart() {
        progress?.visibility = View.VISIBLE
    }

    override fun onLoadEnd() {
        progress?.visibility = View.GONE
    }

    override fun onFullScreenChange(isFullScreen: Boolean) {
        //记录控制器状态
        val visable = controlView?.visibility == View.VISIBLE
        removeAllViews()
        LayoutInflater.from(context).inflate(if (isFullScreen) {
            R.layout.item_video_feed_media_control_land
        } else {
            R.layout.item_video_feed_media_control_port
        }, this)
        configViews()
        if (visable) {
            showMediaControl()
        }
    }
}