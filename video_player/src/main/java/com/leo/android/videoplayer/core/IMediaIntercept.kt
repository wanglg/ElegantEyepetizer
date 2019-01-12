package com.leo.android.videoplayer.core

import android.net.Uri
import android.view.ViewGroup

interface IMediaIntercept {
    /**
     * 拦截播放
     */
    fun interceptPlay(uri: Uri): Boolean

    fun interceptAttachView(): ViewGroup?
}