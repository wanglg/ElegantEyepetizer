package com.android.leo.base

import android.content.Context
import android.graphics.Point
import android.support.v4.app.Fragment
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.agile.android.leo.mvp.BasePresenter


fun Fragment.showToast(content: String): Toast {
    val toast = Toast.makeText(this.activity?.applicationContext, content, Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

fun Context.showToast(content: String): Toast {
    val toast = Toast.makeText(BaseApplication.context, content, Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

fun BasePresenter<*, *>.showToast(content: String): Toast {
    val toast = Toast.makeText(BaseApplication.context, content, Toast.LENGTH_SHORT)
    toast.show()
    return toast
}
//
//fun BasePresenter<*, *>.readStringPreference(key: String): String {
//    val vaule = PreferencesUtil.readString(BaseApplication.context, key, "")
//    return vaule
//}
//
//fun BasePresenter<*, *>.readIntPreference(key: String): Int {
//    val vaule = PreferencesUtil.readString(BaseApplication.context, key, 0)
//    return vaule
//}
//fun BasePresenter<*, *>.readLongPreference(key: String): String {
//    val vaule = PreferencesUtil.readString(BaseApplication.context, key, "")
//    return vaule
//}
fun getScreenWidth(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun getScreenHeight(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

fun View.dip2px(dipValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

fun View.px2dip(pxValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun durationFormat(duration: Long?): String {
    val minute = duration!! / 60
    val second = duration % 60
    return if (minute <= 9) {
        if (second <= 9) {
            "0$minute' 0$second''"
        } else {
            "0$minute' $second''"
        }
    } else {
        if (second <= 9) {
            "$minute' 0$second''"
        } else {
            "$minute' $second''"
        }
    }
}

/**
 * 数据流量格式化
 */
fun Context.dataFormat(total: Long): String {
    var result: String
    var speedReal: Int = (total / (1024)).toInt()
    result = if (speedReal < 512) {
        speedReal.toString() + " KB"
    } else {
        val mSpeed = speedReal / 1024.0
        (Math.round(mSpeed * 100) / 100.0).toString() + " MB"
    }
    return result
}




