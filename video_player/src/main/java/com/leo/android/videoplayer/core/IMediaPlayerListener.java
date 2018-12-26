package com.leo.android.videoplayer.core;


import android.net.Uri;

/**
 * User: wanglg
 * Date: 2016-04-12
 * Time: 16:20
 * FIXME
 */
public interface IMediaPlayerListener {

    void onBufferingUpdate(int percent);

    void onCompletion();

    void onError(int what, int extra, String msg);

    void onFirstFrameStart();

    void onPrepared();

    void updatePlayDuration(long currentDuration, long videoDuration);

    void startPrepare(Uri uri);

    void stopPlayer(boolean isPlayComplete);

    void onInfo(int what, int extra);

    void onLoadProgress(int progress);

    void onLoadStart();

    void onLoadEnd();

    /**
     * @param isFullScreen true进入全屏 false otherwise
     */
    void onFullScreenChange(boolean isFullScreen);
}
