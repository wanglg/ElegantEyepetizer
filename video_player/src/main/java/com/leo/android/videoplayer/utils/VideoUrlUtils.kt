package com.leo.android.videoplayer.utils

import android.net.Uri
import android.text.TextUtils

object VideoUrlUtils {
    fun convertRemoteUrl(videoPath: String): Uri {
        return Uri.parse("common://" + "remote?path=" + Uri.encode(videoPath))
    }

    fun convertUrlToString(uri: Uri): String {
        var path = ""
        val scheme = uri.scheme;
        if (TextUtils.equals(scheme, "common")) {
            val host = uri.host
            if (TextUtils.equals("remote", host)) {
                val videoPath = uri.getQueryParameter("path")
                if (!TextUtils.isEmpty(videoPath)) {
                    path = videoPath as String
                }
            }
        }
        return path
    }

    fun convertAssertUrl(videoPath: String): Uri {
        return Uri.parse("common://" + "assert?path=" + videoPath)
    }
}