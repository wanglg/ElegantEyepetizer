package com.leowong.project.eyepetizer.media;


import android.net.Uri;

/**
 * User: wanglg
 * Date: 2016-04-14
 * Time: 11:33
 * FIXME
 */
public interface IMediaPlayerControl {

    void start();

    /**
     * 清除尝试暂停状态,并开始播放视频
     */
    void tryStart();

    void pause();

    /**
     * 尝试暂停，播放器未准备好情况下，标记播放器状态，在onPrepared时调用暂停
     */
    void tryPause();

    void stop();

    void release();

    boolean isPlaying();

    boolean isPlayComplete();

    long getDuration();

    long getCurrentPosition();

    void seekTo(long pos);

    void setMute(boolean isMute);

    void play(Uri videoUri, Long position);

    int getBufferPercentage();

    boolean isFullScreen();

    void toggleFullScreen();
}
