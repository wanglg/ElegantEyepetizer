package com.leowong.project.eyepetizer.media.ijk

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import com.leowong.project.eyepetizer.BuildConfig
import com.leowong.project.eyepetizer.R
import com.leowong.project.eyepetizer.media.IMediaPlayerControl
import com.leowong.project.eyepetizer.media.IMediaPlayerListener
import com.leowong.project.eyepetizer.utils.LogUtils
import com.leowong.project.eyepetizer.utils.StatusBarUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.TextureMediaPlayer
import java.util.concurrent.TimeUnit

/**
 * User: wanglg
 * Date: 2018-05-11
 * Time: 18:57
 * FIXME
 */
class IjkVideoView : FrameLayout, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnErrorListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayerControl, IMediaPlayer.OnInfoListener, IRenderView.IRenderCallback, IMediaPlayer.OnVideoSizeChangedListener {


    private val TAG: String = "IjkVideoView"
    private var mediaPlayer: IMediaPlayer? = null//播放器
    //TODO  ccvideo://local?path="http:ssajlasd"
    //TODO scheme 代表视频种类 host 代表 远程 本地 assert raw 等 参数path代表路径 可另外添加自定义参数
    var mVideoUri: Uri? = null
    private var iMediaPlayerListeners: ArrayList<IMediaPlayerListener>? = null
    var isPrepared: Boolean = false
    var isTryPause: Boolean = false
    var isCompleted = false//是否播放完成
    //当前播放位置
    var currentPosition: Long? = 0
    //缓冲进度
    var mCurrentBufferPercentage: Int = 0
    var isSurfaceDestroy = false
    var isPlayingOnPause: Boolean = false
    var playScheduleSubscription: CompositeDisposable? = null
    /**
     * 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
     */
    var isFreeze = false
    var renderView: IRenderView? = null
    var controlView: View? = null
    var mSurfaceHolder: IRenderView.ISurfaceHolder? = null
    private var mVideoSarNum: Int = 0
    private var mVideoSarDen: Int = 0
    /**
     * 初始父view
     */
    private var mInitialParent: ViewParent? = null
    private var mInitWidth: Int = 0
    private var mInitHeight: Int = 0
    private var looping: Boolean = false


    constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.layout_ijk_video_view, this)
        initSurface()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_ijk_video_view, this)
        initSurface()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_ijk_video_view, this)
        initSurface()
    }


    fun setVideoUri(videoUri: Uri) {
        this.mVideoUri = videoUri
    }

    fun setVideoPath(videoPath: String) {
        this.mVideoUri = Uri.parse("common://" + "remote?path=" + "ijkhttphook:" + Uri.encode(videoPath))
    }

    fun setAssertPath(videoPath: String) {
        this.mVideoUri = Uri.parse("common://" + "assert?path=" + videoPath)
    }

    fun initSurface() {
        renderView = findViewById<View>(R.id.renderView) as IRenderView
        renderView?.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT)
        renderView?.addRenderCallback(this)
    }

    fun initPlayer() {
        LogUtils.d(TAG, "initPlayer--> " + mVideoUri?.toString())
        if (mediaPlayer != null) {
            resetPlayer()
            mediaPlayer = creatPlayer()
        } else {
            mediaPlayer = creatPlayer()
        }
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnCompletionListener(this)
        mediaPlayer?.setOnErrorListener(this)
        mediaPlayer?.setOnVideoSizeChangedListener(this)
//        mediaPlayer?.setScreenOnWhilePlaying(true) 测试这个方法和预期不符
        mediaPlayer?.isLooping = looping
        bindSurfaceHolder(mediaPlayer, mSurfaceHolder)
        mediaPlayer?.setOnBufferingUpdateListener(this)
        mediaPlayer?.setOnInfoListener(this)
    }

    override fun onCompletion(p0: IMediaPlayer?) {
        LogUtils.d(TAG, "onCompletion--> " + mVideoUri?.toString())
        this.isCompleted = true
        keepScreenOn = false
        iMediaPlayerListeners?.let {
            for (item in it) {
                item.onCompletion()
            }
        }
        currentPosition = 0
    }

    fun setAspectRatio(aspectRatio: Int) {
        renderView?.setAspectRatio(aspectRatio)
    }

    fun setLooping(isLooping: Boolean) {
        this.looping = isLooping
    }

    fun creatPlayer(): IMediaPlayer {
        return createTextureMediaPlayer();
//        return createIjkMediaPlayer();
    }


    fun createTextureMediaPlayer(): TextureMediaPlayer {
        val ijkMediaPlayer = IjkMediaPlayer()
        if (BuildConfig.DEBUG) {
            IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_ERROR)
        }
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 8 * 1024 * 1024)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1);//重连模式
        //断网自动重新连接
        ijkMediaPlayer.setOnNativeInvokeListener(object : IjkMediaPlayer.OnNativeInvokeListener {
            override fun onNativeInvoke(p0: Int, p1: Bundle?): Boolean {
                return true
            }

        })
        return TextureMediaPlayer(ijkMediaPlayer)
    }

    fun createIjkMediaPlayer(): IjkMediaPlayer {
        val ijkMediaPlayer = IjkMediaPlayer()
        if (BuildConfig.DEBUG) {
            IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_ERROR)
        }
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 8 * 1024 * 1024)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1);//重连模式
        //断网自动重新连接
        ijkMediaPlayer.setOnNativeInvokeListener(object : IjkMediaPlayer.OnNativeInvokeListener {
            override fun onNativeInvoke(p0: Int, p1: Bundle?): Boolean {
                return true
            }

        })
        return ijkMediaPlayer
    }

    fun addMediaPlayerListener(iMediaPlayerListener: IMediaPlayerListener) {
        if (iMediaPlayerListeners == null) {
            iMediaPlayerListeners = ArrayList();
        }
        this.iMediaPlayerListeners?.add(iMediaPlayerListener)
    }

    fun startPlay() {
        mVideoUri?.let {
            startPlay(it)
        }

    }

    fun startPlay(videoUri: Uri) {
        startPlay(videoUri, 0)
    }

    override fun onPrepared(p0: IMediaPlayer?) {
        LogUtils.d(TAG, "onPrepared  ")
//        bindSurfaceHolder(mediaPlayer, mSurfaceHolder)
        isPrepared = true
        iMediaPlayerListeners?.let {
            for (item in it) {
                item.onPrepared()
            }
        }
        LogUtils.d(TAG, "mediaPlayer videoWidth->" + mediaPlayer!!.videoWidth + " mediaPlayer videoHeight->" + mediaPlayer!!.videoHeight)
        renderView?.setVideoSize(mediaPlayer!!.videoWidth, mediaPlayer!!.videoHeight)
        renderView?.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
        if (!isFreeze) {
            //恢复到原来位置
            if (currentPosition!! > 0L) {
                seekTo(currentPosition!!)
            }
            if (isTryPause) {
                pause()
                isTryPause = false
            } else {
                start()
            }
        } else {
            pause()
        }
        sendPlayPosition()
    }

    fun onResume() {
        LogUtils.d(TAG, "onResume  " + mVideoUri?.toString())
        if (isFreeze) {
            isFreeze = false
            if (isPrepared) {
                start()
            }
        } else if (isPrepared) {
//            start()//暂停状态下会有黑屏情况，改为恢复状态继续播放
            if (isPlayingOnPause) {
                start()
            } else {
//                seekTo(currentPosition!!)
                pause()
            }
        }
    }

    fun onPause() {
        LogUtils.d(TAG, "onPause  " + mVideoUri?.toString())
        if (isPrepared) {
            savePlayerState()
            currentPosition = mediaPlayer?.currentPosition
            pause()
        } else {
            // 如果播放器没有prepare完成，则设置isFreeze为true
            LogUtils.d(TAG, "播放器没有prepare完成")
            isFreeze = true
        }

    }

    override fun onBufferingUpdate(p0: IMediaPlayer?, p1: Int) {
        //p1 是百分比 0->100
//        LogUtils.d(TAG, "onBufferingUpdate->" + p1)
        mCurrentBufferPercentage = p1
        iMediaPlayerListeners?.let {
            for (item in it) {
                item.onBufferingUpdate(p1)
            }
        }
    }

    override fun release() {
        stop()
        mediaPlayer?.release()
        mediaPlayer = null
        playScheduleSubscription?.clear()
        isPrepared = false
    }

    fun onDestory() {
        release()
        playScheduleSubscription?.clear()
    }


    //TODO 播放视频
    fun startPlay(videoDetail: Uri, seekPosition: Long) {
        LogUtils.d(videoDetail.toString())
        initPlayer()
        isPrepared = false
        isCompleted = false
        currentPosition = seekPosition
        mCurrentBufferPercentage = 0
        try {
            val scheme = videoDetail.scheme;
            if (TextUtils.equals(scheme, "common")) {
                val host = videoDetail.host
                if (TextUtils.equals("remote", host)) {
                    val videoPath = videoDetail.getQueryParameter("path")
                    if (!TextUtils.isEmpty(videoPath)) {
                        mediaPlayer?.dataSource = videoPath
                        mediaPlayer?.prepareAsync()
                        iMediaPlayerListeners?.let {
                            for (item in it) {
                                item.startPrepare(videoDetail)
                            }
                        }
                    }
                } else if (TextUtils.equals("assert", host)) {
                    val videoPath = videoDetail.getQueryParameter("path")
                    val am = context.getAssets()
                    val afd = am.openFd(videoPath)
                    val rawDataSourceProvider = RawDataSourceProvider(afd)
                    mediaPlayer?.setDataSource(rawDataSourceProvider);
                    mediaPlayer?.prepareAsync()
                    iMediaPlayerListeners?.let {
                        for (item in it) {
                            item.startPrepare(videoDetail)
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            LogUtils.e(TAG, ex.message!!)
        }


    }

    override fun onInfo(p0: IMediaPlayer?, arg1: Int, arg2: Int): Boolean {
        iMediaPlayerListeners?.let {
            for (item in it) {
                item.onInfo(arg1, arg2)
            }
        }
        if (arg1 == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            LogUtils.d(TAG, "视频角度为---》" + arg2)
            if (arg2 != 0) {
                renderView?.setVideoRotation(arg2);
            }
        }
        if (arg1 == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            iMediaPlayerListeners?.let {
                for (item in it) {
                    item.onFirstFrameStart()
                }
            }
        }
        if (arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            iMediaPlayerListeners?.let {
                for (item in it) {
                    item.onLoadStart()
                }
            }
        }
        if (arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            iMediaPlayerListeners?.let {
                for (item in it) {
                    item.onLoadEnd()
                }
            }
        }
        return true
    }


    override fun onVideoSizeChanged(mp: IMediaPlayer?, width: Int, height: Int, sarNum: Int, sarDen: Int) {
        LogUtils.d("onVideoSizeChanged width->" + width + " height->" + height + " sarNum->" + sarNum + " sarDen->" + sarDen)
        mp?.let {
            val mVideoWidth = it.getVideoWidth()
            val mVideoHeight = it.getVideoHeight()
            mVideoSarNum = mp.videoSarNum
            mVideoSarDen = mp.videoSarDen
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                if (renderView != null) {
                    renderView?.setVideoSize(mVideoWidth, mVideoHeight)
                    renderView?.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
                }
                requestLayout()
            }
        }

    }

    override fun onSurfaceCreated(holder: IRenderView.ISurfaceHolder, width: Int, height: Int) {
        LogUtils.d(TAG, "surfaceCreated  " + mVideoUri?.toString())
        isSurfaceDestroy = false
        mSurfaceHolder = holder
        mediaPlayer?.let {
            bindSurfaceHolder(it, holder)
        }
    }


    override fun onSurfaceChanged(holder: IRenderView.ISurfaceHolder, format: Int, width: Int, height: Int) {
        LogUtils.d(TAG, "onSurfaceTextureSizeChanged")
    }

    override fun onSurfaceDestroyed(holder: IRenderView.ISurfaceHolder) {
        LogUtils.d(TAG, "surfaceDestroyed  " + mVideoUri?.toString())
        isSurfaceDestroy = true
        mSurfaceHolder = null
        releaseWithoutStop()
    }

    private fun releaseWithoutStop() {
        mediaPlayer?.setDisplay(null)
    }

    // REMOVED: mSHCallback
    private fun bindSurfaceHolder(mp: IMediaPlayer?, holder: IRenderView.ISurfaceHolder?) {
        if (mp == null)
            return

        if (holder == null) {
            mp.setDisplay(null)
            return
        }

        holder.bindToMediaPlayer(mp)
        //如果暂停状态下，surface被销毁,调用seekTo防止黑屏
//        if (isPrepared && !isPlayingOnPause) {
//            seekTo(currentPosition!!)
//        }
    }

    private fun resetPlayer() {
        playScheduleSubscription?.clear()
        stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        //TextureView不能复用，每次加载下一个video的时候都会把前一个TextureView移除掉，然后新建一个TextureView
        removeView(renderView as View)
        LayoutInflater.from(context).inflate(R.layout.layout_ijk_video_view, this)
        initSurface()
    }


    fun sendPlayPosition() {
        playScheduleSubscription?.clear()
        if (playScheduleSubscription == null) {
            playScheduleSubscription = CompositeDisposable()
        }
        playScheduleSubscription?.add(Flowable.interval(1, TimeUnit.SECONDS).filter(object : Predicate<Long> {
            override fun test(t: Long): Boolean {
                return mediaPlayer != null && isPrepared && !isSurfaceDestroy && isPlaying && mediaPlayer?.duration!! > 0
            }

        }).map(object : Function<Long, Long> {
            override fun apply(t: Long): Long {
                try {
                    return mediaPlayer?.currentPosition ?: 0
                } catch (e: Exception) {
                    e.printStackTrace()
                    return 0L
                }

            }

        }).filter(object : Predicate<Long> {
            override fun test(t: Long): Boolean {
                return t > 0
            }

        }).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<Long> {
                    override fun accept(t: Long?) {
                        currentPosition = t;
                        iMediaPlayerListeners?.let {
                            for (item in it) {
                                item.updatePlayDuration(currentPosition!!, mediaPlayer?.duration!!)
                            }
                        }
                    }

                }))
    }


    //保存播放状态
    private fun savePlayerState() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                isPlayingOnPause = true
            } else {
                isPlayingOnPause = false
            }
        }
    }

    override fun start() {
        if (isPrepared) {
            LogUtils.d(TAG, "start-->" + mVideoUri?.toString())
            mediaPlayer?.start()
            keepScreenOn = true
        }
    }

    override fun tryStart() {
        isTryPause = false
        if (isPrepared) {
            mediaPlayer?.start()
            keepScreenOn = true
        } else {
            startPlay()
        }
    }

    override fun pause() {
        if (isPrepared) {
            LogUtils.d(TAG, "pause-->" + mVideoUri?.toString())
            mediaPlayer?.pause()
            keepScreenOn = false
        }
    }

    override fun stop() {
        if (isPrepared) {
            mediaPlayer?.stop()
            keepScreenOn = false
            LogUtils.d(TAG, "stop-->" + mVideoUri?.toString())
            iMediaPlayerListeners?.let {
                for (item in it) {
                    item.stopPlayer(isPlayComplete)
                }
            }
        }
    }

    override fun tryPause() {
        if (isPrepared) {
            mediaPlayer?.pause()
            keepScreenOn = false
        } else {
            isTryPause = true
        }
    }

    override fun isPlaying(): Boolean {
        if (mediaPlayer != null && isPrepared) {
            return mediaPlayer?.isPlaying!!
        } else {
            return false
        }
    }

    override fun isPlayComplete(): Boolean {
        return isCompleted
    }

    override fun getDuration(): Long {
        if (isPrepared) {
            return mediaPlayer?.duration ?: 0
        } else {
            return 0
        }
    }

    override fun getCurrentPosition(): Long {
        if (isPrepared) {
            return mediaPlayer?.currentPosition ?: 0
        } else {
            return 0
        }

    }

    override fun seekTo(pos: Long) {
        LogUtils.d(TAG, "seekTo-->" + pos)
        if (!isPrepared) {
            return
        } else {
            mediaPlayer?.seekTo(pos)
        }
    }

    override fun play(videoDetail: Uri?, position: Long) {
        startPlay(videoDetail!!, position)
    }

    override fun getBufferPercentage(): Int {
        if (mediaPlayer != null) {
            return mCurrentBufferPercentage
        } else {
            return 0
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogUtils.d(TAG, "onConfigurationChanged  " + mVideoUri?.toString())
        if (context == null) {
            return
        }
        if (isFullScreen) {
            setScreenFull(true)
        } else if (context != null && mediaPlayer != null) {
            setScreenFull(false)
        }
    }

    override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        iMediaPlayerListeners?.let {
            for (item in it) {
                item.onError(p1, p2, "")
            }
        }
        return true
    }

    override fun isFullScreen(): Boolean {
        return context != null && (context as Activity).requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun toggleFullScreen() {
        savePlayerState()
        if (isFullScreen) {
            switchScreenOrientation(1)
        } else {
            mInitialParent = parent
            mInitWidth = this.width
            mInitHeight = this.height
            switchScreenOrientation(2)
        }
    }

    /**
     * 横竖屏切换
     *
     * @param type 1竖屏，2横屏，0自动切换
     */
    private fun switchScreenOrientation(type: Int) {
        if (context == null) {
            return
        }
        if (type == 1) {
            if ((context as Activity).requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        } else if (type == 2) {
            if ((context as Activity).requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        } else {
            if ((context as Activity).requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    fun attachMediaControl(controlView: View) {
        if (this.controlView != null) {
            this.removeView(this.controlView)
            this.controlView = null
        }
        this.controlView = controlView
        addView(controlView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
    }

    private fun setScreenFull(isEnterFullScreen: Boolean) {
        val parent = this.parent ?: return
        (parent as ViewGroup).removeView(this)
        if (isEnterFullScreen) {
            val mDecorView = (context as Activity).window.decorView
            (mDecorView as ViewGroup).addView(this, -1, ViewGroup.LayoutParams(-1, -1))

        } else {
            (mInitialParent as ViewGroup).addView(this, -1, ViewGroup.LayoutParams(mInitWidth, mInitHeight))
        }
        StatusBarUtils.setUiFlags(context as Activity, isEnterFullScreen)
    }

}