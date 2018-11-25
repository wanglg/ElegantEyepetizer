package com.leo.android.videplayer.utils

import android.net.Uri

object VideoUrlUtils {
    fun convertRemoteUrl(videoPath: String): Uri {
        return Uri.parse("common://" + "remote?path=" + Uri.encode(videoPath))
    }

    fun convertAssertUrl(videoPath: String): Uri {
        return Uri.parse("common://" + "assert?path=" + videoPath)
    }
}