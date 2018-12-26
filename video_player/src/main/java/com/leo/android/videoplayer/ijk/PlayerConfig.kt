package com.leo.android.videoplayer.ijk

class PlayerConfig private constructor() {
    var isLooping: Boolean = false//是否循环播放
    var mAutoRotate: Boolean = false//是否旋转屏幕
    var isCache: Boolean = false//是否开启缓存
    var reConnect: Boolean = false//是否开启重连机制
    var calculateMatch: Boolean = false//计算宽高填满View
    var disableAudioFocus: Boolean = false//关闭AudioFocus监听
    var aspectRatio: Int = IRenderView.AR_ASPECT_FIT_PARENT//视频裁剪模式

    class Builder {
        var target: PlayerConfig = PlayerConfig()

        /**
         * 开启缓存 PS:启用缓存可能对播放器事件回调有影响，除非列表小视频，其它不建议启用缓存
         */
        fun enableCache(): Builder {
            target.isCache = true
            return this
        }

        /**
         * 设置自动旋转
         */
        fun autoRotate(): Builder {
            target.mAutoRotate = true
            return this
        }

        fun reConnect(reconnect: Boolean): Builder {
            target.reConnect = reconnect
            return this
        }

        fun setAspectRatio(aspectRatio: Int): Builder {
            target.aspectRatio = aspectRatio
            return this
        }

        /**
         * 开启循环播放
         */
        fun setLooping(): Builder {
            target.isLooping = true
            return this
        }

        fun calculateMatch(): Builder {
            target.calculateMatch = true
            return this
        }

        fun build(): PlayerConfig {
            return target
        }

        /**
         * 关闭AudioFocus监听
         */
        fun disableAudioFocus(): Builder {
            target.disableAudioFocus = true
            return this
        }

    }

}