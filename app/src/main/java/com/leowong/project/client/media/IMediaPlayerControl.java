package com.leowong.project.client.media;


import com.leowong.project.client.media.model.VideoDetail;

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

    void play(VideoDetail videoDetail, Long position);

    int getBufferPercentage();

//    boolean isFullScreen();
//    void toggleFullScreen();
}
