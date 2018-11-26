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
        videoView.id = R.id.video_view
    }

    class Builder {
        var target: PlayerListManager

        constructor(context: Context) {
            target = PlayerListManager(context)
        }

        fun playerConfig(playerConfig: PlayerConfig): Builder {
            target.playConfig = playerConfig
            return this
        }

        fun preLoad(preLoad: Boolean): Builder {
            target.preLoad = preLoad
            return this
        }

        fun build(): PlayerListManager {
            return target
        }

    }

    fun attachView(parentView: ViewGroup) {
        this.parentView = parentView
    }

    fun onPause() {
        videoView.onPause()
    }

    fun onResume() {
        videoView.onResume()
    }

    fun go() {
        parentView?.addView(videoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        playConfig?.let {
            videoView.setPlayerConfig(it)
        }
        currentPath?.let {
            videoView.startPlay(it)
        }
    }
}