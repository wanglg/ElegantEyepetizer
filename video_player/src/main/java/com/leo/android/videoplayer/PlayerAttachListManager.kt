package com.leo.android.videoplayer

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import com.danikula.videocache.HttpProxyCacheServer
import com.leo.android.videoplayer.core.BaseVideoController
import com.leo.android.videoplayer.core.IMediaIntercept
import com.leo.android.videoplayer.core.IMediaPlayerControl
import com.leo.android.videoplayer.core.IMediaPlayerListener
import com.leo.android.videoplayer.ijk.PlayerConfig

/**
 * attach模式
 */
class PlayerAttachListManager : IMediaPlayerControl {


    val TAG = "PlayerAttachListManager"
    override fun start() {
        videoView.start()
    }

    override fun setMediaIntercept(mediaIntercept: IMediaIntercept) {
        videoView.setMediaIntercept(mediaIntercept)
    }

    override fun pause() {
        videoView.pause()
    }

    override fun play() {
        videoView.play()
    }

    override fun stop() {
        videoView.stop()
    }

    override fun reset() {
        videoView.reset()
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

    override fun play(videoUri: Uri) {
        videoView.play(videoUri)
    }

    override fun setMute(isMute: Boolean) {
        videoView.setMute(isMute)
    }

    override fun detachMediaControl() {
        videoView.detachMediaControl()
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

    override fun preLoad(videoUri: Uri?) {
        videoView.preLoad(videoUri)
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
        var target: PlayerAttachListManager

        constructor(context: Context) {
            target = PlayerAttachListManager(context)
        }

        fun playerConfig(playerConfig: PlayerConfig): Builder {
            target.playConfig = playerConfig
            return this
        }

        fun preLoad(preLoad: Boolean): Builder {
            target.preLoad = preLoad
            return this
        }

        fun build(): PlayerAttachListManager {
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

    fun onDestory() {
        videoView.release()
        if (preLoad) {

        }
    }

    fun go() {
        parentView?.addView(videoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        playConfig?.let {
            videoView.setPlayerConfig(it)
        }
        currentPath?.let {
            videoView.play(it, 0)
            if (preLoad) {
//                nextPath?.let {
//                    val videoPath = VideoUrlUtils.convertUrlToString(it)
//                    val isCache = VideoCacheManager.getProxy(videoView.context).isCached(videoPath)
//                    if (!isCache) {
//                        VideoCacheManager.getProxy(videoView.context).registerCacheListener(object : CacheListener {
//                            override fun onCacheAvailable(cacheFile: File?, url: String, percentsAvailable: Int) {
//                                LogUtils.d(TAG, "url->" + url + " size ->" + percentsAvailable)
//                            }
//
//                        }, videoPath)
//                    }
//                }


            }
        }
    }
}