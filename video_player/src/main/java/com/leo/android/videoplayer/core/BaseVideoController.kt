package com.leo.android.videoplayer.core

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * 控制View层
 */
abstract class BaseVideoController(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), IMediaPlayerListener {


    open var videoControl: IMediaPlayerControl? = null

    open fun setMediaControl(player: IMediaPlayerControl) {
        player.attachMediaControl(this)
    }

    /**
     * 重置状态
     */
    abstract fun resetView()
}