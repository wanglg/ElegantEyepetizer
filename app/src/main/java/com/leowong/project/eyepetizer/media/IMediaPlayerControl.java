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

    void tryStart();

    void pause();

    /**
     * 尝试暂停，播放器未准备好情况下，在onPrepared时调用暂停
     */
    void tryPause();

    void stop();

    void release();

    boolean isPlaying();

    boolean isPlayComplete();

    long getDuration();

    long getCurrentPosition();

    void seekTo(int pos);

    void play(Uri videoUri, Long position);

    int getBufferPercentage();

//    boolean isFullScreen();
//    void toggleFullScreen();
}
