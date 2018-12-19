package com.android.leo.toutiao.ui.widget

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.model.entity.News
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leo.android.videplayer.core.BaseVideoController
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class VideoFeedItemController : BaseVideoController {
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
    var videoCoverTitle: TextView? = null
    var videoCoverLayout: RelativeLayout? = null

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
        videoCoverTitle = findViewById(R.id.video_cover_title)
//        if (!TextUtils.isEmpty(title)) {
//            videoTitleTv?.setText(title)
//        }
        mCurrentTime = findViewById(R.id.time_current)
        mVideoDuration = findViewById(R.id.duration)
        seekbar = findViewById(R.id.mediacontroller_progress)
        videoCover = findViewById(R.id.vidoeCover)
        backImg = findViewById(R.id.media_player_back)
        videoCoverLayout = findViewById(R.id.video_cover_layout)
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

    fun setNew(news: News) {
        this.news = news
        videoCoverTitle?.setText(news.title)
        videoTitleTv?.setText(news.title)
        videoCover?.let {
            val coverOption = ImageLoaderOptions.Builder(it, news.video_detail_info.detail_video_large_image.url)
                    .isCrossFade(true).build()
            ImageLoader.showImage(coverOption)
        }
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

    override fun updatePlayDuration(currentDuration: Long, videoDuration: Long) {
        seekbar?.setProgress((currentDuration * 100 / videoDuration).toInt())
        seekbar?.setSecondaryProgress((videoControl?.bufferPercentage!!))
        mVideoDuration?.setText(stringForTime(videoDuration))
        mCurrentTime?.setText(stringForTime(currentDuration))
    }

    override fun startPrepare(uri: Uri?) {
        videoCoverLayout?.visibility = View.GONE
        isPrepared = false
        progress?.visibility = View.VISIBLE
    }

    override fun stopPlayer(isPlayComplete: Boolean) {
        videoCoverLayout?.visibility = View.VISIBLE
        videoCover?.visibility = View.VISIBLE
        isPrepared = false
        cancel()
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
            R.layout.item_video_feed_media_control_port
        } else {
            R.layout.item_video_feed_media_control_land
        }, this)
        configViews()
        if (visable) {
            showMediaControl()
        }
    }
}