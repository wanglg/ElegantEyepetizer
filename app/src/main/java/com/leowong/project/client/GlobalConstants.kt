package com.leowong.project.client

import android.os.Environment
import java.io.File

//常量
object GlobalConstants {
    //统计回调地址 以后要改成自己服务器的域名？
    const val INNO_ADDRESS = "https://log.yiwantv.com"//http://report.test.amita.xin
    //设置缓存目录
    val PLAY_CACHE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath +
            File.separator + "playablestudio" + File.separator + "cache"
    //设置视频观看缓存目录
    val PLAYVIDEO_DOWNLOAD_CACHE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath +
            File.separator + "playablestudio" + File.separator + "videoCache" + File.separator
    const val ACCESS_TOKEN = "access-token"
    const val IS_FIRST_LAUNCH = "is_first_launch"
    const val IS_NEW_INSTALL_USER = "is_new_install_user"
    const val QR_DOWNLOAD_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.playablestudio.caowt"
    const val QUESTION_MARK = "?"
    const val EQUAL_SIGN = "="
    const val AND = "&"
    const val ID = "id"
    const val OPENID = "openid"
    const val LOGIN = "login"
    const val ACTION = "action"
    const val VALUE = "value"
    const val JUMP_COMMAND = "jump_command"
    const val MEMBER_ID = "member_id"
    const val CH = "ch"
    const val P_STUDIO = "pstudio"
    const val NEW = "new"
    const val ADD = "add"
    const val DEL = "del"
    const val UID = "uid"
    const val TID = "tid"
    const val TITLE = "title"
    const val URL = "url"
    const val URL_TYPE = "url_type"
    const val PROFILE = "profile"
    const val ACT = "act"
    const val FANS = "fans"
    const val USER_ID = "user_id"
    const val TIME = "time"
    const val PAGE = "page"
    const val COUNT = "count"
    const val LEVEL = "level"
    const val DESC = "desc"
    const val JOB_ID = "job_id"
    const val DATA = "data"
    const val SERIES_ID = "series_id"
    const val IS_FINISH = "is_finish"
    const val QR_URL = "qr_url"
    const val RESULT_ID = "result_id"
    const val WORLD_CUP_IS_OPEN = "world_cup_is_open"

    const val START_REPORT_PROGRESS = 1000
    const val DO_REPORT_PROGRESS = 2000
    const val STOP_REPORT_PROGRESS = 3000

    const val EDIT_USER_NAME = "edit_user_name"

    const val REQUEST_COLLECTION = 10001
    const val REQUEST_RELEASE = 10002
    const val REQUEST_TV = 10003
    const val VID = "vid"

    const val VIDEO_LIST_TYPE_SEARCH_GAME = 1000
    const val VIDEO_LIST_TYPE_SEARCH_TV = 1001
    const val VIDEO_LIST_TYPE_SEARCH = 1002
    const val VIDEO_LIST_TYPE_SEARCH_COLLECTION = 1003
    const val VIDEO_LIST_TYPE_SEARCH_RELEASE = 1004

    const val REQUEST_STORAGE_PERMISSIONS = 1000
    const val REQUEST_LOCATIONE_PERMISSIONS = 1001
    const val REQUEST_PHONE_STATE_PERMISSIONS = 1002
    const val REQUEST_CAMERA_PERMISSIONS = 1003
    const val REQUEST_RECORD_AUDIO_PERMISSIONS = 1004

    const val SHARED_PREFERENCES = "sharedpreferences"
    const val INTEGER = 1
    const val LONG = 2
    const val BOOLEAN = 3
    const val FLOAT = 4
    const val STRING = 5
    const val STRING_SET = 6
    const val BOOLEAN_TRUE = 7

    const val DOWNLOAD_URL_KEY = "url" //上传成功
    const val DOWNLOAD_APKNAME_KEY = "apkName" //上传成功
    const val DOWNLOAD_APKPATH_KEY = "apkPath" //上传成功

    const val KEY_WORD = "key_word"
    const val CONTENT = "content"
    const val HISTORY = "history"
    const val SEARCH_HISTORY_LENGTH = 20


    const val USER_AGENT = "User-Agent"
    const val PLAYABLE_CLIENT = "playableClient "//设备唯一标识
    const val VERSION_NAME = "version_name"
    const val YW_CHANNEL_TYPE = "yw-channel-type"//渠道来源
    const val YW_DEVICE_MODEL = "yw-device-model"//设备类型 安卓-android IOS-iOS
    const val YW_DEVICE_VERSION = "yw-device-version"//设备 sdk 版本号

    const val DEVICE_MODEL = "android"
    const val DOWNLOAD_URL = "downloadUrl"

    const val MID = "mid"
    const val DURATION = "duration"

    const val GAME_ID = "game_id"
    const val TV_ID = "tv_id"
    const val VIDEO_ID = "video_id"
    const val PAGE_NUMBER = "page_number"
    const val COMMENT_CONTENT = "content"
    const val PAGE_SIZE = "page_size"
    const val OFFSET = "offset"
    const val SIZE = "size"
    const val IS_NEW = "is_new"


    const val OPEN_ID = "open_id"

    const val NICKNAME = "nickname"
    const val AVATAR = "avatar"
    const val TYPE = "type"
    const val GENDER = "gender"


    const val LOGIN_WEIXIN = 2
    const val LOGIN_QQ = 1
    const val LOGIN_SINA_WEIBO = 3

    const val KEY_CURRENT_USER = "key_current_user";
    const val KEY_LOGIN_INFO = "key_login_info";
    const val KEY_LOGIN = "key_login";
    const val YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd";

    const val COMMENTRESULTKEY = "commentresultkey"

    const val SHARE_ITEM_WECHAT = 1
    const val SHARE_ITEM_CIRCLE_FRIEND = 2
    const val SHARE_ITEM_REPORT = 3
    const val SHARE_ITEM_DISLIKE = 4
    const val SHARE_ITEM_DELETE = 5
    //详情页播放有效时长统计 每5秒报一次
    const val REPORT_PLAY_TIME = 5

    //视频观看详情列表相关
    const val WATCH_VIDEO_LIST_TYPE_KEY = "watch_video_list_type_key"//从哪来
    const val WATCH_VIDEO_LIST_SCROLL_KEY = "watch_video_list_scroll_key"//滑动到第几页
    const val WATCH_VIDEO_LIST_DATA_KEY = "watch_video_list_data_key"//带过来的数据
    const val WATCH_VIDEO_LIST_PAGE_KEY = "watch_video_list_page_key"//第几页
    const val WATCH_VIDEO_LIST_UID_KEY = "watch_video_list_uid_key"//uid
    const val WATCH_VIDEO_LIST_SEARCH_KEY = "watch_video_list_search_key"//搜索关键字
    const val WATCH_VIDEO_LIST_GAME_KEY = "watch_video_list_game_key"//gameid
    const val WATCH_VIDEO_LIST_TV_KEY = "watch_video_list_tv_key"//tvid

    //作品
    const val WATCH_VIDEO_LIST_TYPE_PRODUCTION = 0
    //收藏
    const val WATCH_VIDEO_LIST_TYPE_COLLECT = 1
    //TV
    const val WATCH_VIDEO_LIST_TYPE_TV = 2
    //GAME
    const val WATCH_VIDEO_LIST_TYPE_GAME = 3
    //搜索
    const val WATCH_VIDEO_LIST_TYPE_SEARCH = 4
    //GAME_推荐
    const val WATCH_VIDEO_LIST_TYPE_GAME_RECOMMEND = 5

    const val SHARE_MEDIA_STATUS = "share_media_status"
    const val SHARE_TO_WEIXIN_FRIEND = 1000
    const val SHARE_TO_WEIXIN = 2000
    const val SHARE_TO_QQ = 3000
    const val SHARE_TO_SINA = 4000

    const val SUCCESS_SHARE = "success_share"
    const val ACTIVITY_SHARE = 1002
    const val SUBTITLE_KEY = "subtitle_key"
    const val DANMU_SWITCH_KEY = "danmu_switch_key"
    const val IS_SHOW_CUP_CARD_TIP = "is_show_cup_card_tip"
    const val CUP_STAR_CARD_KEY = "cup_star_card_key"
    //是否有未领取红包
    const val IS_HAVE_UNRECEIVED_PACKET = "is_have_unreceived_packet"

    object NetworkType {
        const val NO_NETWORK: Int = 0
        const val MOBILE_NETWORK: Int = 1
        const val WIFI_NETWORK: Int = 2
    }

    object VersionUpgrade {
        const val PARAMS_CHANNEL_TYPE = "channel_type"
        const val PARAMS_DEVICE_MODEL = "device_model"
        const val PARAMS_APP_VERSION = "device_version"
    }


}
