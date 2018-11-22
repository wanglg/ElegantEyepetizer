package com.leo.android.videplayer

import android.net.Uri
import com.leo.android.videplayer.ijk.PlayerConfig

class PlayerListManager {
    var currentPath: Uri? = null
    var prePath: Uri? = null
    var nextPath: Uri? = null
    var preLoad: Boolean = false
    var playConfig: PlayerConfig? = null
}