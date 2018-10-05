package com.leowong.project.eyepetizer.ui.view.widgets

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.media.IMediaPlayerControl
import com.leowong.project.eyepetizer.media.IMediaPlayerListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class VideoDetailMediaControlView : FrameLayout, IMediaPlayerListener {
    var progress: ProgressBar? = null
    var seekbar: SeekBar? = null
    var videoCover: ImageView? = null
    var backImg: ImageView? = null
    var controlView: View? = null
    var isPrepared: Boolean = false

    var videoControl: IMediaPlayerControl? = null
    var coverPath: String? = null
    var controlDisposable: Disposable? = null

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.item_video_detail_media_control, this)
        configViews()
    }

    fun configViews() {
        progress = findViewById(R.id.loading_progress)
        seekbar = findViewById(R.id.mediacontroller_progress)
        videoCover = findViewById(R.id.vidoeCover)
        backImg = findViewById(R.id.media_player_back)
        controlView = findViewById(R.id.control_hierarchy)
        backImg?.setOnClickListener {
            if (context != null) {
                (context as Activity).finish()
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

        seekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                cancel()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                videoControl?.seekTo(seekBar.progress)
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
        seekbar?.max = videoControl?.duration!!.toInt()
    }

    override fun updatePlayDuration(currentDuration: Long, videoDuration: Long) {
        seekbar?.setProgress(currentDuration.toInt())
        seekbar?.setSecondaryProgress((videoControl?.bufferPercentage!!))
    }

    override fun startPrepare(uri: Uri?) {
        isPrepared = false
        progress?.visibility = View.VISIBLE
    }

    /* override fun onTouchEvent(event: MotionEvent): Boolean {
         if (isPrepared) {
             if (event.getAction() == MotionEvent.ACTION_DOWN) {
                 toggleMediaControlsVisiblity()
             } else if (event.getAction() == MotionEvent.ACTION_UP) {
                 performClick()
             }

         }
         return super.onTouchEvent(event)
     }*/


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