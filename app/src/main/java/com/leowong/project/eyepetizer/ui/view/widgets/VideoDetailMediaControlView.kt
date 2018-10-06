package com.leowong.project.eyepetizer.ui.view.widgets

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.media.IMediaPlayerControl
import com.leowong.project.eyepetizer.media.IMediaPlayerListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class VideoDetailMediaControlView : FrameLayout, IMediaPlayerListener {
    var progress: ProgressBar? = null
    var seekbar: SeekBar? = null
    var videoCover: ImageView? = null
    var backImg: ImageView? = null
    var pauseOrPlay: ImageView? = null
    var fullscreen: ImageView? = null
    var controlView: View? = null
    var isPrepared: Boolean = false

    var videoControl: IMediaPlayerControl? = null
    var coverPath: String? = null
    var controlDisposable: Disposable? = null
    var mFormatBuilder: StringBuilder? = null
    var mFormatter: Formatter? = null
    var mVideoDuration: TextView? = null
    var mCurrentTime: TextView? = null

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.item_video_detail_media_control, this)
        configViews()
    }

    fun configViews() {
        mFormatBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        progress = findViewById(R.id.loading_progress)
        fullscreen = findViewById(R.id.fullscreen)
        pauseOrPlay = findViewById(R.id.pauseOrPlay)
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
                videoControl?.seekTo(seekBar.progress * videoControl?.duration!!.toInt() / 100)
                showMediaControl()
            }

        })
    }


    fun setMediaControl(player: IMediaPlayerControl) {
        videoControl = player
    }

    fun setVideoCover(cover: String) {
        coverPath = cover
        val coverOption = ImageLoaderOptions.Builder(videoCover!!, coverPath)
                .placeholder(R.drawable.placeholder_banner).isCrossFade(true).build()
        ImageLoader.showImage(coverOption)
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
//        seekbar?.max = videoControl?.duration!!.toInt()
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


    private fun toggleMediaControlsVisiblity() {
        if (controlView?.visibility == View.GONE) {
            showMediaControl()
        } else {
            controlView?.visibility = View.GONE
        }
    }

    override fun stopPlayer(isPlayComplete: Boolean) {
        cancel()
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

}