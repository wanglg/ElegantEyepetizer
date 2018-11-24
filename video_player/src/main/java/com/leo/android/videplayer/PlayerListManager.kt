package com.leo.android.videplayer

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import com.danikula.videocache.HttpProxyCacheServer
import com.leo.android.videplayer.core.BaseVideoController
import com.leo.android.videplayer.core.IMediaPlayerControl
import com.leo.android.videplayer.core.IMediaPlayerListener
import com.leo.android.videplayer.ijk.PlayerConfig

class PlayerListManager : IMediaPlayerControl {
    override fun start() {
        videoView.start()
    }

    override fun pause() {
        videoView.pause()
    }

    override fun stop() {
        videoView.stop()
    }

    override fun isPlaying(): Boolean {
        return videoView.isPlaying
    }

    override fun isPlayComplete(): Boolean {
        return videoView.isPlayComplete
    }

    override fun getDuration(): Long {
        return videoView.duration
    }

    override fun getCurrentPosition(): Long {
        return videoView.getCurrentPosition()
    }

    override fun seekTo(pos: Long) {
        videoView.seekTo(pos)
    }

    override fun setMute(isMute: Boolean) {
        videoView.setMute(isMute)
    }

    override fun play(videoUri: Uri, position: Long) {
        videoView.play(videoUri, position)
    }

    override fun getBufferPercentage(): Int {
        return videoView.bufferPercentage
    }

    override fun isFullScreen(): Boolean {
        return videoView.isFullScreen
    }

    override fun toggleFullScreen() {
        videoView.toggleFullScreen()
    }

    override fun setLock(isLocked: Boolean) {
        videoView.setLock(isLocked)
    }

    override fun getLockState(): Boolean {
        return videoView.lockState
    }

    override fun attachMediaControl(baseVideoController: BaseVideoController) {
        videoView.attachMediaControl(baseVideoController)
    }

    override fun addMediaPlayerListener(iMediaPlayerListener: IMediaPlayerListener) {
        videoView.addMediaPlayerListener(iMediaPlayerListener)
    }

    override fun release() {
        videoView.release()
        parentView?.removeView(videoView)
        videoView.detachMediaControl()
    }

    var currentPath: Uri? = null
    var prePath: Uri? = null
    var nextPath: Uri? = null
    var preLoad: Boolean = false
    var playConfig: PlayerConfig? = null
    var mCacheServer: HttpProxyCacheServer? = null
    var parentView: ViewGroup? = null
    val videoView: IjkVideoView
//    var videoController: BaseVideoController? = null

    private constructor(context: Context) {
        videoView = IjkVideoView(context)
    }

    class Builder {
        var target: PlayerListManager

        constructor(context: Context) {
            target = PlayerListManager(context)
        }

        fun attachView(parentView: ViewGroup) {
            target.parentView = parentView
        }

        fun playerConfig(playerConfig: PlayerConfig) {
            target.playConfig = playerConfig
        }

//        fun setVideoController(videoController: BaseVideoController) {
//            target.videoController = videoController
//        }

        fun preLoad(preLoad: Boolean) {
            target.preLoad = preLoad
        }

        fun build(): PlayerListManager {
            return target
        }

    }


    fun go() {
        release()
        parentView?.addView(videoView)
        playConfig?.let {
            videoView.setPlayerConfig(it)
        }
        currentPath?.let {
            videoView.startPlay(it)
        }
    }
}