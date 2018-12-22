package com.android.leo.toutiao.utils

import android.util.Base64
import com.android.leo.toutiao.api.ApiConstant
import com.android.leo.toutiao.api.ApiService
import com.android.leo.toutiao.api.RepositoryManager
import com.android.leo.toutiao.mvp.model.entity.Video
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.regex.Pattern
import java.util.zip.CRC32

object VideoPathParser {

    fun decodePath(srcUrl: String): Observable<String> {
        return RepositoryManager.obtainRetrofitService(ApiService::class.java).getVideoHtml(srcUrl).flatMap { html ->
            val pattern = Pattern.compile("videoId: '(.+)'")
            val matcher = pattern.matcher(html)
            if (matcher.find()) {
                val videoId = matcher.group(1)
                //1.将/video/urls/v/1/toutiao/mp4/{videoid}?r={Math.random()}，进行crc32加密。
                val r = getRandom()
                val crc32 = CRC32()
                val s = String.format(ApiConstant.URL_VIDEO, videoId, r)
                //进行crc32加密。
                crc32.update(s.toByteArray())
                val crcString = crc32.value.toString() + ""
                val url = ApiConstant.HOST_VIDEO + s + "&s=" + crcString
                RepositoryManager.obtainRetrofitService(ApiService::class.java).getVideoData(url)
            } else {
                throw Exception("解析异常")
            }

        }.map {
            val data = it.data.video_list
            if (data.video_3 != null) {
                updateVideo(data.video_3)
            } else if (data.video_2 != null) {
                updateVideo(data.video_2)
            } else if (data.video_1 != null) {
                updateVideo(data.video_1)
            } else {
                throw Exception("解析异常")
            }
        }.map {
            it.main_url
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    }

    private fun getRealPath(base64: String): String {
        return String(Base64.decode(base64.toByteArray(), Base64.DEFAULT))
    }

    private fun updateVideo(video: Video): Video {
        //base64解码
        video.main_url = getRealPath(video.main_url)
        return video
    }

    private fun getRandom(): String {
        val random = Random()
        val result = StringBuilder()
        for (i in 0..15) {
            result.append(random.nextInt(10))
        }
        return result.toString()
    }

}




