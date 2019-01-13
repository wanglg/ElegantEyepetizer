package com.leo.android.videoplayer.core;


import android.net.Uri;

/**
 * 抽象播放控制层,播放器实现
 * User: wanglg
 * Date: 2016-04-14
 * Time: 11:33
 * FIXME
 */
public interface IMediaPlayerControl {

    void start();

    void pause();

    void stop();

    void reset();

    void release();

    boolean isPlaying();

    boolean isPlayComplete();

    long getDuration();

    long getCurrentPosition();

    void seekTo(long pos);

    /**
     * 静音
     *
     * @param isMute
     */
    void setMute(boolean isMute);

    void play(Uri videoUri, Long position);

    void play();

    void play(Uri videoUri);

    void preLoad(Uri videoUri);

    int getBufferPercentage();

    boolean isFullScreen();

    void toggleFullScreen();

    /**
     * 设置是否锁定屏幕
     *
     * @param isLocked true 锁定屏幕，禁止自动旋转 false otherwise
     */
    void setLock(boolean isLocked);

    boolean getLockState();

    /**
     * @param baseVideoController
     */
    void attachMediaControl(BaseVideoController baseVideoController);

    /**
     * detach 控制view
     */
    void detachMediaControl();

    void addMediaPlayerListener(IMediaPlayerListener iMediaPlayerListener);

    void setMediaIntercept(IMediaIntercept mediaIntercept);

}
