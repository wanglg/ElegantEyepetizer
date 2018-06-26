package com.leowong.project.client.media.model

import com.leowong.project.client.base.BaseBeanModel


/**
 * 视频信息

 */

class VideoDetail : BaseBeanModel() {
    /**
     * 发布者昵称
     */
    var nickname: String? = null
    var video_url: String? = null
    /**
     * 转载url
     */
    var source_url: String? = null
    /**
     * 发布者头像URL
     */
    var avatar_url: String? = null
    /**
     * 发布者uid
     */
    var user_id: Int? = 0
    var vip_type: Int? = 0

    var video_id: Int = 0

    var tv_id: Int = 0
    /**
     * 是否订阅电视台(0否 1是)
     */
    var is_subscribe_tv: Int = 0
    /**
     * 是否收藏(0否 1是)
     */
    var is_favourite: Int = 0
    /**
     * 是否有字幕(0否 1是)
     */
    var is_subtitle: Int = 0

    var tv_name: String? = null
    /**
     * 视频宽高
     */
    var video_cover_width: Int = 0

    var video_cover_height: Int = 0

    var video_cover_share_url: String = ""

    var video_cover_origin_url: String = ""

    /**
     * 视频标题
     */
    var title: String = ""
    /**
     * 类型 1 原创 2 转载
     */
    var type: Int? = 2
    var status: Int? = 2
    var status_info: String = ""
    var comment_count: Int? = 0
    var fav_count: Int? = 0//
    var share_count: Int? = 0//
    var video_cover_url: String? = null


}
