package com.leo.android.videoplayer

import android.net.Uri
import com.leo.android.videoplayer.core.BaseVideoController
import com.leo.android.videoplayer.core.IMediaIntercept
import com.leo.android.videoplayer.core.IMediaPlayerControl
import com.leo.android.videoplayer.core.IMediaPlayerListener

class PlayerListManager : IMediaPlayerControl {


    val TAG = "PlayerListManager"
    override fun start() {
        videoView?.start()
    }

    override fun pause() {
        videoView?.pause()
    }

    override fun stop() {
        videoView?.stop()
    }

    override fun play(videoUri: Uri) {
        videoView?.play()
    }

    override fun reset() {
        videoView?.reset()
    }

    override fun setMediaIntercept(mediaIntercept: IMediaIntercept) {
        videoView?.setMediaIntercept(mediaIntercept)
    }

    override fun play() {
        videoView?.play()
    }

    override fun isPlaying(): Boolean {
        if (videoView == null) {
            return false
        } else {
            return videoView!!.isPlaying
        }
    }

    override fun isPlayComplete(): Boolean {
        if (videoView == null) {
            return false
        } else {
            return videoView!!.isPlayComplete
        }

    }

    override fun getDuration(): Long {
        if (videoView == null) {
            return 0
        } else {
            return videoView!!.duration
        }
    }

    override fun detachMediaControl() {
        videoView?.detachMediaControl()
    }

    override fun getCurrentPosition(): Long {
        if (videoView == null) {
            return 0
        } else {
            return videoView!!.getCurrentPosition()
        }

    }

    override fun seekTo(pos: Long) {
        videoView?.seekTo(pos)
    }

    override fun setMute(isMute: Boolean) {
        videoView?.setMute(isMute)
    }

    override fun play(videoUri: Uri, position: Long) {
        videoView?.play(videoUri, position)
    }

    override fun getBufferPercentage(): Int {
        if (videoView == null) {
            return 0
        } else {
            return videoView!!.bufferPercentage
        }

    }

    override fun isFullScreen(): Boolean {
        if (videoView == null) {
            return false
        } else {
            return videoView!!.isFullScreen
        }
    }

    override fun toggleFullScreen() {
        videoView?.toggleFullScreen()
    }

    override fun preLoad(videoUri: Uri?) {
        videoView?.preLoad(videoUri)
    }

    override fun setLock(isLocked: Boolean) {
        videoView?.setLock(isLocked)
    }

    override fun getLockState(): Boolean {
        if (videoView == null) {
            return false
        } else {
            return videoView!!.lockState
        }

    }

    override fun attachMediaControl(baseVideoController: BaseVideoController) {
        videoView?.attachMediaControl(baseVideoController)
    }

    override fun addMediaPlayerListener(iMediaPlayerListener: IMediaPlayerListener) {
        videoView?.addMediaPlayerListener(iMediaPlayerListener)
    }

    override fun release() {
        videoView?.release()
        videoView = null
    }

    fun onPause() {
        videoView?.onPause()
    }

    fun onResume() {
        videoView?.onResume()
    }

    fun onDestory() {
        videoView?.onDestory()
    }

    var videoView: IjkVideoView? = null

    fun setCurrentVideoView(videoView: IjkVideoView) {
        release()
        this.videoView = videoView
    }


}