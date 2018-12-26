package com.leo.android.videoplayer

import android.net.Uri
import com.leo.android.videoplayer.core.IMediaPlayerListener

open class SimpleMediaPlayerListener : IMediaPlayerListener {
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

    override fun onFullScreenChange(isFullScreen: Boolean) {
    }
}