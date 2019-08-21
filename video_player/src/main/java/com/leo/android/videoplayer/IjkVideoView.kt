package com.leo.android.videoplayer

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.danikula.videocache.CacheListener
import com.danikula.videocache.HttpProxyCacheServer
import com.leo.android.videoplayer.cache.VideoCacheManager
import com.leo.android.videoplayer.core.BaseVideoController
import com.leo.android.videoplayer.core.IMediaIntercept
import com.leo.android.videoplayer.core.IMediaPlayerControl
import com.leo.android.videoplayer.core.IMediaPlayerListener
import com.leo.android.videoplayer.ijk.IRenderView
import com.leo.android.videoplayer.ijk.PlayerConfig
import com.leo.android.videoplayer.ijk.RawDataSourceProvider
import com.leo.android.videoplayer.utils.LogUtils
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.TextureMediaPlayer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Author: wanglg
 * Date: 2018-05-11
 * Time: 18:57
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
    /**
     * 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
     */
    var isFreeze = false
    var renderView: IRenderView? = null
    var controlView: BaseVideoController? = null
    var mSurfaceHolder: IRenderView.ISurfaceHolder? = null
    /**
     * 初始父view
     */
    private var mInitialParent: ViewParent? = null
    private var mInitWidth: Int = 0
    private var mInitHeight: Int = 0
    /**
     * 是否静音
     */
    private var isMute: Boolean = false
    private var mAudioManager: AudioManager? = null//系统音频管理器
    /**
     * 播放器配置
     */
    private var playerConfig: PlayerConfig = PlayerConfig.Builder().build()
    private var mAudioFocusHelper: AudioFocusHelper? = null

    private var mCacheServer: HttpProxyCacheServer? = null
    /**
     * 是否锁定屏幕
     */
    protected var isLockFullScreen: Boolean = false
    /**
     * 是否锁定传感器，防止太快
     */
    protected var isLockOrientation: Boolean = false


    /**
     * 更新播放位置计时调度器
     */
    var es: ScheduledExecutorService? = null
    /**
     * 用于取消计时任务
     */
    var future: ScheduledFuture<*>? = null
    /**
     * 拦截逻辑
     */
    var iMediaIntercept: IMediaIntercept? = null

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
        this.mVideoUri = Uri.parse("common://" + "remote?path=" + Uri.encode(videoPath))
    }

    fun setAssertPath(videoPath: String) {
        this.mVideoUri = Uri.parse("common://" + "assert?path=" + videoPath)
    }

    fun initSurface() {
        renderView = findViewById<View>(R.id.renderView) as IRenderView
        renderView?.addRenderCallback(this)
    }

    fun initPlayer() {
        LogUtils.d(TAG, "initPlayer--> " + mVideoUri?.toString())
        if (mediaPlayer != null) {
            release()
            mediaPlayer = creatPlayer()
        } else {
            mediaPlayer = creatPlayer()
        }
        if (!playerConfig.disableAudioFocus) {
            mAudioManager = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            mAudioFocusHelper = AudioFocusHelper()
        }
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnCompletionListener(this)
        mediaPlayer?.setOnErrorListener(this)
        mediaPlayer?.setOnVideoSizeChangedListener(this)
//        mediaPlayer?.setScreenOnWhilePlaying(true) 测试这个方法和预期不符
        mediaPlayer?.isLooping = playerConfig.isLooping
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
                //更新播放进度
                item.updatePlayDuration(mediaPlayer?.duration!!, mediaPlayer?.duration!!)
                item.onCompletion()
            }
        }
        currentPosition = 0
    }


    fun setPlayerConfig(playerConfig: PlayerConfig) {
        this.playerConfig = playerConfig
    }

    fun creatPlayer(): IMediaPlayer {
        return createTextureMediaPlayer();
//        return createIjkMediaPlayer();
    }


    fun createTextureMediaPlayer(): TextureMediaPlayer {
        val ijkMediaPlayer = IjkMediaPlayer()
        if (BuildConfig.DEBUG) {
            IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_WARN)
        }
        ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
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
        ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 8 * 1024 * 1024)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1);//重连模式
//        //断网自动重新连接
        ijkMediaPlayer.setOnNativeInvokeListener(object : IjkMediaPlayer.OnNativeInvokeListener {
            override fun onNativeInvoke(p0: Int, p1: Bundle?): Boolean {
                return true
            }

        })
        return ijkMediaPlayer
    }

    override fun addMediaPlayerListener(iMediaPlayerListener: IMediaPlayerListener) {
        if (iMediaPlayerListeners == null) {
            iMediaPlayerListeners = ArrayList();
        }
        this.iMediaPlayerListeners?.add(iMediaPlayerListener)
    }

    override fun setMediaIntercept(mediaIntercept: IMediaIntercept) {
        this.iMediaIntercept = mediaIntercept
    }

    fun removeMediaPlayerListener(iMediaPlayerListener: IMediaPlayerListener) {
        this.iMediaPlayerListeners?.remove(iMediaPlayerListener)
    }

    fun removeAllMediaPlayerListener() {
        this.iMediaPlayerListeners?.clear()
    }

    override fun play() {
        mVideoUri?.let {
            play(it)
        }
    }

    override fun play(videoUri: Uri) {
        play(videoUri, 0)
    }

    override fun preLoad(videoUri: Uri?) {
        videoUri?.let {

        }
    }

    override fun onPrepared(p0: IMediaPlayer?) {
        LogUtils.d(TAG, "onPrepared  ")
        isPrepared = true
        iMediaPlayerListeners?.let {
            for (item in it) {
                item.onPrepared()
            }
        }
        val videoWidth = mediaPlayer!!.videoWidth
        val videoHeight = mediaPlayer!!.videoHeight
        LogUtils.d(TAG, "mediaPlayer videoWidth->" + videoWidth + " mediaPlayer videoHeight->" + videoHeight)
        renderView?.setVideoSize(videoWidth, videoHeight)
        if (playerConfig.mAutoRotate) {
            isLockOrientation = false
            orientationEventListener.enable()
        }
        if (playerConfig.calculateMatch) {
            val videoRate = videoWidth.toFloat() / videoHeight
            LogUtils.d(TAG, "videoRate->" + videoRate)
            val viewWidth = width
            val viewHeight = height
            val viewRate = viewWidth.toFloat() / viewHeight
            if (Math.abs(viewRate - videoRate) < 0.15) {
                renderView?.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT)
            } else {
                renderView?.setAspectRatio(playerConfig.aspectRatio)
            }
            LogUtils.d(TAG, "viewRate->" + viewRate)
        } else {
            renderView?.setAspectRatio(playerConfig.aspectRatio)
        }
        if (!isFreeze) {
            //恢复到原来位置
            if (currentPosition!! > 0L) {
                seekTo(currentPosition!!)
            }
            if (isTryPause) {
                pause()
                isTryPause = false
            }
            else {
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
            if (isPlayingOnPause) {
                start()
            } else {
                pause()
            }
        }
        if (playerConfig.mAutoRotate) {
            orientationEventListener.enable()
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
        if (playerConfig.mAutoRotate) {
            orientationEventListener.disable()
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
        LogUtils.d(TAG, "start release->")
        reset()
        mediaPlayer?.release()
        LogUtils.d(TAG, "release over->")
        //防止多个视频源监听干扰
        removeAllMediaPlayerListener()
        mediaPlayer = null
        cancelSendSchedule()
        isPrepared = false
    }

    fun onDestory() {
        release()
    }


    /**
     * 播放视频
     */
    override fun play(videoUri: Uri, seekPosition: Long) {
        LogUtils.d(videoUri.toString())
        if (context == null) {
            return
        }
        setVideoUri(videoUri)
        //拦截播放
        if (iMediaIntercept != null && mVideoUri != null) {
            if (iMediaIntercept?.interceptPlay(mVideoUri!!)!!) {
                return
            }
        }
        initPlayer()
        isPrepared = false
        isCompleted = false
        currentPosition = seekPosition
        mCurrentBufferPercentage = 0
        //添加控制面板监听播放器状态
        if (controlView != null && iMediaPlayerListeners != null && !iMediaPlayerListeners!!.contains(controlView!!)) {
            iMediaPlayerListeners?.add(controlView!!)
        }
        try {
            val scheme = videoUri.scheme;
            if (TextUtils.equals(scheme, "common")) {
                val host = videoUri.host
                if (TextUtils.equals("remote", host)) {
                    var videoPath = videoUri.getQueryParameter("path")
                    if (!TextUtils.isEmpty(videoPath)) {
                        if (playerConfig.isCache) {//启用边播放边缓存功能
                            if (mCacheServer == null) {
                                mCacheServer = getCacheServer()
                            }
                            val preUrl = videoPath
                            videoPath = mCacheServer?.getProxyUrl(videoPath)
                            LogUtils.d(TAG, "ProxyUrl--》" + videoPath)
                            mCacheServer?.registerCacheListener(cacheListener, preUrl)
                            if (mCacheServer?.isCached(preUrl)!!) {
                                mCurrentBufferPercentage = 100
                                //已缓存成功的去掉buff监听
                                mediaPlayer?.setOnBufferingUpdateListener(null)
                            } else {
                                videoPath = "ijkhttphook:" + videoPath//自动重连播放功能
                            }
                        } else {
                            videoPath = "ijkhttphook:" + videoPath//自动重连播放功能
                        }
                        LogUtils.d(TAG, "mediaPlayer path--》" + videoPath)
                        mediaPlayer?.dataSource = videoPath
                        iMediaPlayerListeners?.let {
                            for (item in it) {
                                item.startPrepare(videoUri)
                            }
                        }
                        mediaPlayer?.prepareAsync()
                    }
                } else if (TextUtils.equals("assert", host)) {
                    val videoPath = videoUri.getQueryParameter("path") as String
                    val am = context.getAssets()
                    val afd = am.openFd(videoPath)
                    val rawDataSourceProvider = RawDataSourceProvider(afd)
                    mediaPlayer?.setDataSource(rawDataSourceProvider);
                    iMediaPlayerListeners?.let {
                        for (item in it) {
                            item.startPrepare(videoUri)
                        }
                    }
                    mediaPlayer?.prepareAsync()
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
                LogUtils.d(TAG, "onFirstFrameStart---》" + arg2)
                for (item in it) {
                    item.onFirstFrameStart()
                }
            }
        }
        if (arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            LogUtils.d(TAG, "BUFF---》start")
            iMediaPlayerListeners?.let {
                for (item in it) {
                    item.onLoadStart()
                }
            }
        }
        if (arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            LogUtils.d(TAG, "BUFF---》end")
            iMediaPlayerListeners?.let {
                for (item in it) {
                    item.onLoadEnd()
                }
            }
        }
        return true
    }


    override fun onVideoSizeChanged(mp: IMediaPlayer?, width: Int, height: Int, sarNum: Int, sarDen: Int) {
        LogUtils.d(TAG, "onVideoSizeChanged width->" + width + " height->" + height + " sarNum->" + sarNum + " sarDen->" + sarDen)
        mp?.let {
            val mVideoWidth = it.getVideoWidth()
            val mVideoHeight = it.getVideoHeight()
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                if (renderView != null) {
                    renderView?.setVideoSize(mVideoWidth, mVideoHeight)
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
    }

    override fun reset() {
        cancelSendSchedule()
        stop()
        mediaPlayer?.reset()
        removeView(renderView as View)
        LayoutInflater.from(context).inflate(R.layout.layout_ijk_video_view, this)
        initSurface()
    }

    private val updatePositionRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null && isPrepared && !isSurfaceDestroy && isPlaying && mediaPlayer?.duration!! > 0) {
                try {
                    val position = mediaPlayer?.currentPosition
                    currentPosition = position
                    val duration = mediaPlayer?.duration
                    iMediaPlayerListeners?.let {
                        for (item in it) {
                            item.updatePlayDuration(currentPosition!!, duration!!)
                        }
                    }
                } catch (e: Exception) {
                    LogUtils.e(e.localizedMessage)
                }
            }
        }

    }


    fun sendPlayPosition() {
        cancelSendSchedule()
        if (es == null) {
            es = Executors.newScheduledThreadPool(10)
        }
        future = es?.scheduleAtFixedRate({
            try {
                if (mediaPlayer != null && isPrepared && !isSurfaceDestroy && isPlaying && mediaPlayer?.duration!! > 0) {
                    val position = mediaPlayer?.currentPosition ?: 0
                    if (position > 0) {
                        post(updatePositionRunnable)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, 0, 1L, TimeUnit.SECONDS)
    }

    private fun cancelSendSchedule() {
        future?.cancel(true)
        future = null
        removeCallbacks(updatePositionRunnable)
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
        if (iMediaIntercept != null && mVideoUri != null) {
            if (iMediaIntercept?.interceptPlay(mVideoUri!!)!!) {
                return
            }
        }
        isTryPause = false
        if (isPrepared) {
            LogUtils.d(TAG, "start-->" + mVideoUri?.toString())
            mediaPlayer?.start()
            mAudioFocusHelper?.requestFocus()
            keepScreenOn = true
        }
    }

    override fun pause() {
        if (isPrepared) {
            LogUtils.d(TAG, "pause-->" + mVideoUri?.toString())
            mediaPlayer?.pause()
            mAudioFocusHelper?.abandonFocus()
            keepScreenOn = false
        } else {
            isTryPause = true
        }
    }

    override fun stop() {
        if (isPrepared) {
            mediaPlayer?.stop()
            keepScreenOn = false
            mAudioFocusHelper?.abandonFocus()
            LogUtils.d(TAG, "stop-->" + mVideoUri?.toString())
        }
        iMediaPlayerListeners?.let {
            for (item in it) {
                item.stopPlayer(isPlayComplete)
            }
        }
        mCacheServer?.unregisterCacheListener(cacheListener)
        if (playerConfig.mAutoRotate) {
            orientationEventListener.disable()
            isLockOrientation = false
            removeCallbacks(lockRunnable)
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

    override fun setMute(isMute: Boolean) {
        if (mediaPlayer != null) {
            this.isMute = isMute
            val volume = if (isMute) 0.0f else 1.0f
            mediaPlayer?.setVolume(volume, volume)
        }
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

    override fun getBufferPercentage(): Int {
        if (mediaPlayer != null) {
            return mCurrentBufferPercentage
        } else {
            return 0
        }
    }


    override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        LogUtils.e(TAG, "p1-->" + p1 + "  p2->" + p2)
        iMediaPlayerListeners?.let {
            for (item in it) {
                item.onError(p1, p2, "")
            }
        }
        return true
    }

    override fun isFullScreen(): Boolean {
        return context != null && ((context as Activity).requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                || (context as Activity).requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
    }

    override fun toggleFullScreen() {
        //锁定方向传感器一秒
        if (playerConfig.mAutoRotate) {
            lockOrientationTransitory()
        }
        if (isFullScreen) {
            switchScreenOrientation(1)
        } else {
            switchScreenOrientation(2)
        }
    }

    /**
     * 横竖屏切换
     *
     * @param type 1竖屏，2横屏，3、横屏反向、0自动切换
     */
    private fun switchScreenOrientation(type: Int) {
        if (context == null) {
            return
        }

        if (type == 1) {
            if ((context as Activity).requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                setScreenFull(false)
            }
        } else if (type == 2) {
            if ((context as Activity).requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                if (!isFullScreen) {//记录非全屏状态下一些数据
                    savePortraitData()
                }
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                setScreenFull(true)
            }
        } else if (type == 3) {
            if ((context as Activity).requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                if (!isFullScreen) {//记录非全屏状态下一些数据
                    savePortraitData()
                }
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                setScreenFull(true)
            }
        } else {
            if ((context as Activity).requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                setScreenFull(false)
            } else {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                setScreenFull(true)
            }
        }

    }

    private val lockRunnable = object : Runnable {
        override fun run() {
            isLockOrientation = false
        }
    }

    private fun releaseOrientation() {
        removeCallbacks(lockRunnable)
        postDelayed(lockRunnable, 1000)
    }

    fun savePortraitData() {
        mInitialParent = parent
        mInitWidth = this.width
        mInitHeight = this.height
        LogUtils.d(TAG, "mInitWidth->" + mInitWidth + ", mInitHeight->" + mInitHeight)
    }


    override fun attachMediaControl(baseVideoController: BaseVideoController) {
        detachMediaControl()
        this.controlView = baseVideoController
        addMediaPlayerListener(baseVideoController)
        baseVideoController.videoControl = this
        addView(controlView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
    }

    fun haveControlView(): Boolean {
        return this.controlView != null
    }

    fun getVideoController(): BaseVideoController? {
        return this.controlView
    }

    override fun detachMediaControl() {
        if (this.controlView != null) {
            this.controlView?.resetView()
            this.removeView(this.controlView)
            this.controlView = null
        }
    }

    private fun setScreenFull(isEnterFullScreen: Boolean) {
        val parent = this.parent ?: return
        (parent as ViewGroup).removeView(this)
        if (isEnterFullScreen) {
            val mDecorView = (context as Activity).window.decorView
            (mDecorView as ViewGroup).addView(this, -1, ViewGroup.LayoutParams(-1, -1))
        } else if (iMediaIntercept?.interceptAttachView() != null) {
            val v = iMediaIntercept?.interceptAttachView()
            v?.addView(this, -1, ViewGroup.LayoutParams(-1, -1))
        } else if (mInitialParent != null) {
            (mInitialParent as ViewGroup).addView(this, -1, ViewGroup.LayoutParams(mInitWidth, mInitHeight))
        }
        setUiFlags(context as Activity, isEnterFullScreen)
        iMediaPlayerListeners?.let {
            for (item in it) {
                item.onFullScreenChange(isEnterFullScreen)
            }
        }
    }

    override fun setLock(isLocked: Boolean) {
        this.isLockFullScreen = isLocked
    }

    override fun getLockState(): Boolean {
        return this.isLockFullScreen
    }

    /**
     * 音频焦点改变监听
     */
    inner class AudioFocusHelper : AudioManager.OnAudioFocusChangeListener {

        internal var currentFocus = 0

        /**
         * Requests to obtain the audio focus
         *
         * @return True if the focus was granted
         */
        fun requestFocus(): Boolean {
            if (currentFocus == AudioManager.AUDIOFOCUS_GAIN) {
                return true
            }

            if (mAudioManager == null) {
                return false
            }

            val status = mAudioManager?.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
                currentFocus = AudioManager.AUDIOFOCUS_GAIN
                return true
            }

            return false
        }

        /**
         * Requests the system to drop the audio focus
         *
         * @return True if the focus was lost
         */
        fun abandonFocus(): Boolean {

            if (mAudioManager == null) {
                return false
            }

            val status = mAudioManager?.abandonAudioFocus(this)
            currentFocus = 0
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status
        }

        override fun onAudioFocusChange(focusChange: Int) {
            if (currentFocus == focusChange) {
                return
            }

            currentFocus = focusChange
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN//获得焦点
                    , AudioManager.AUDIOFOCUS_GAIN_TRANSIENT//暂时获得焦点
                -> {

                    if (mediaPlayer != null && !isMute) {
                        //恢复音量
                        mediaPlayer?.setVolume(1.0f, 1.0f)
                    }

                }
                AudioManager.AUDIOFOCUS_LOSS//焦点丢失
                    , AudioManager.AUDIOFOCUS_LOSS_TRANSIENT//焦点暂时丢失
                -> if (isPlaying()) {
                    pause()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK//此时需降低音量
                -> if (mediaPlayer != null && isPlaying && !isMute) {
                    mediaPlayer?.setVolume(0.1f, 0.1f)
                }
            }
        }
    }

    private fun getCacheServer(): HttpProxyCacheServer {
        return VideoCacheManager.getProxy(context.applicationContext)
    }

    /**
     * 缓存监听
     */
    private val cacheListener = CacheListener { cacheFile, url, percentsAvailable ->
        //        mCurrentBufferPercentage = percentsAvailable
        LogUtils.d(TAG, url + " cache-->" + percentsAvailable)
    }

    /**
     * 加速度传感器监听
     */
    protected var orientationEventListener: OrientationEventListener = object : OrientationEventListener(context) { // 加速度传感器监听，用于自动旋转屏幕
        override fun onOrientationChanged(orientation: Int) {
            if (context == null || isLockFullScreen || isLockOrientation) return
            if (orientation >= 340) { //屏幕顶部朝上
                LogUtils.d(TAG, "屏幕顶部朝上")
                lockOrientationTransitory()
                switchScreenOrientation(1)
            } else if (orientation >= 260 && orientation <= 280) { //屏幕左边朝上
                LogUtils.d(TAG, "屏幕左边朝上")
                lockOrientationTransitory()
                switchScreenOrientation(2)
            } else if (orientation >= 70 && orientation <= 90) { //屏幕右边朝上
                LogUtils.d(TAG, "屏幕右边朝上")
                lockOrientationTransitory()
                switchScreenOrientation(3)
            }
        }
    }

    /**
     * 暂时锁定方向传感器
     */
    fun lockOrientationTransitory() {
        isLockOrientation = true
        releaseOrientation()
    }

    /**
     * 播放器全屏处理
     */
    fun setUiFlags(activity: Activity, fullscreen: Boolean) {
        val win = activity.getWindow()
        val winParams = win.getAttributes()
        if (fullscreen) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        }
        win.setAttributes(winParams)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = activity.getWindow().getDecorView()
            if (decorView != null) {
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                decorView.setSystemUiVisibility(if (fullscreen) getFullscreenUiFlags() else option)
            }
        }
    }

    /**
     * 获取全屏flag
     */
    fun getFullscreenUiFlags(): Int {
        var flags = View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flags = flags or (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
        return flags
    }

}