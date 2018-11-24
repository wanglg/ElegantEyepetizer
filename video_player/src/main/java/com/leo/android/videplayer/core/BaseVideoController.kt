package com.leo.android.videplayer.core

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * 控制View层
 */
abstract class BaseVideoController(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), IMediaPlayerListener {


    open var videoControl: IMediaPlayerControl? = null

    fun setMediaControl(player: IMediaPlayerControl) {
        videoControl = player
        player.attachMediaControl(this)
    }

    /**
     * 重置状态
     */
    protected abstract fun reset()
}