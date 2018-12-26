package com.shortvideo.android.leo.ui.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.leo.android.videoplayer.core.BaseVideoController
import com.leowang.shortvideo.R
import com.shortvideo.android.leo.ui.view.widgets.ClipProgressBar

class ShortVideoControlView : BaseVideoController {
    var playImg: ImageView? = null
    var clipProgressBar: ClipProgressBar? = null

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.sv_item_short_video_control, this)
        configViews()
    }

    fun configViews() {
        playImg = findViewById(R.id.playImg)
        clipProgressBar = findViewById(R.id.clipProgressBar)
        playImg?.setOnClickListener({
            videoControl?.start()
            playImg?.visibility = View.GONE
        })
        playImg?.visibility = View.GONE
        setOnClickListener({
            if (videoControl?.isPlaying!!) {
                videoControl?.pause()
                playImg?.visibility = View.VISIBLE
            }
        })
    }

    override fun onFullScreenChange(isFullScreen: Boolean) {
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onCompletion() {
    }

    override fun onError(what: Int, extra: Int, msg: String?) {
    }

    override fun onFirstFrameStart() {
        clipProgressBar?.visibility = View.GONE
    }

    override fun onPrepared() {
    }

    override fun updatePlayDuration(currentDuration: Long, videoDuration: Long) {
    }

    override fun startPrepare(uri: Uri?) {
        clipProgressBar?.visibility = View.VISIBLE
    }

    override fun stopPlayer(isPlayComplete: Boolean) {
    }

    override fun onInfo(what: Int, extra: Int) {
    }

    override fun onLoadProgress(progress: Int) {
    }

    override fun onLoadStart() {
        clipProgressBar?.visibility = View.VISIBLE
    }

    override fun onLoadEnd() {
        clipProgressBar?.visibility = View.GONE
    }

    override fun resetView() {
        playImg?.visibility = View.GONE
    }
}