package com.shortvideo.android.leo.ui.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import com.leo.android.videplayer.core.BaseVideoController
import com.leowang.shortvideo.R

class ShortVideoControlView : BaseVideoController {
    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.sv_item_short_video_control, this)
        configViews()
    }

    fun configViews() {

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

    override fun onLoadProgress(progress: Int) {
    }

    override fun onLoadStart() {
    }

    override fun onLoadEnd() {
    }

    override fun reset() {
    }
}