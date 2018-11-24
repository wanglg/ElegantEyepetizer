package com.leo.android.videplayer

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import com.danikula.videocache.HttpProxyCacheServer
import com.leo.android.videplayer.ijk.PlayerConfig

class PlayerListManager {
    var currentPath: Uri? = null
    var prePath: Uri? = null
    var nextPath: Uri? = null
    var preLoad: Boolean = false
    var playConfig: PlayerConfig? = null
    var mCacheServer: HttpProxyCacheServer? = null
    var parentView: ViewGroup? = null
    val videoView: IjkVideoView

    private constructor(context: Context) {
        videoView = IjkVideoView(context)
    }

    class Builder {
        var target: PlayerListManager

        constructor(context: Context) {
            target = PlayerListManager(context)
        }

        fun attachView(parentView: ViewGroup){
            target.parentView=parentView
        }

        fun build(): PlayerListManager {
            return target
        }

    }

    fun play() {
        playConfig?.let {
            videoView.setPlayerConfig(it)
        }

    }
}