package com.leowong.project.client.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

object ActivityUtil {

    /**
     * title :        设置Activity全屏显示
     * description :设置Activity全屏显示。
     *
     * @param activity Activity引用
     * @param isFull   true为全屏，false为非全屏
     */
    fun setFullScreen(activity: Activity, isFull: Boolean) {
        val window = activity.window
        val params = window.attributes
        if (isFull) {
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            window.attributes = params
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else {
            params.flags = params.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            window.attributes = params
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    fun setDarkStatusIcon(decorView: View, bDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var vis = decorView.systemUiVisibility
            if (bDark) {
                vis = vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis = vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = vis
        }
    }

    /**
     * title :        隐藏系统标题栏
     * description :隐藏Activity的系统默认标题栏
     *
     * @param activity Activity对象
     */
    fun hideTitleBar(activity: Activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    /**
     * title :        设置Activity的显示方向为垂直方向
     * description :强制设置Actiity的显示方向为垂直方向。
     *
     * @param activity Activity对象
     */
    fun setScreenVertical(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * title :        设置Activity的显示方向为横向
     * description :强制设置Actiity的显示方向为横向。
     *
     * @param activity Activity对象
     */
    fun setScreenHorizontal(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * title :      隐藏软件输入法
     * description :隐藏软件输入法
     * time :       -- 下午::
     *
     * @param activity
     */
    fun hideSoftInput(activity: Activity) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    /**
     * title :        使UI适配输入法
     * description :使UI适配输入法
     * time :     -- 下午::
     *
     * @param activity
     */
    fun adjustSoftInput(activity: Activity) {
        activity.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    /**
     * title :        跳转到某个Activity。
     * description :跳转到某个Activity
     * time :     -- 下午::
     *
     * @param activity       本Activity
     * @param targetActivity 目标Activity的Class
     */
    fun switchTo(activity: Activity, targetActivity: Class<out Activity>) {
        switchTo(activity, Intent(activity, targetActivity))
    }

    /**
     * title :        根据给定的Intent进行Activity跳转
     * description :根据给定的Intent进行Activity跳转
     * time :     -- 下午::
     *
     * @param activity Activity对象
     * @param intent   要传递的Intent对象
     */
    fun switchTo(activity: Activity, intent: Intent) {
        activity.startActivity(intent)
    }

    /**
     * title :        带参数进行Activity跳转
     * description :带参数进行Activity跳转
     * time :     -- 下午::
     *
     * @param activity       Activity对象
     * @param targetActivity 目标Activity的Class
     * @param params         跳转所带的参数
     */
    fun switchTo(activity: Activity, targetActivity: Class<out Activity>, params: Map<String, Any>?) {
        if (null != params) {
            val intent = Intent(activity, targetActivity)
            for ((key, value) in params) {
                setValueToIntent(intent, key, value)
            }
            switchTo(activity, intent)
        }
    }

    //    /**
    //     * title :  带参数进行Activity跳转
    //     * description :带参数进行Activity跳转
    //     * time :     -- 下午::
    //     *
    //     * @param activity
    //     * @param target
    //     * @param params
    //     */
    //    public static void switchTo(Activity activity, Class<? extends Activity> target, NameValuePair... params) {
    //        if (null != params) {
    //            Intent intent = new Intent(activity, target);
    //            for (NameValuePair param : params) {
    //                setValueToIntent(intent, param.getName(), param.getValue());
    //            }
    //            switchTo(activity, intent);
    //        }
    //    }


    /**
     * title :        将值设置到Intent里
     * description :将值设置到Intent里
     * time :     -- 下午::
     *
     * @param intent Inent对象
     * @param key    Key
     * @param val    Value
     */
    private fun setValueToIntent(intent: Intent, key: String, `val`: Any) {
        if (`val` is Boolean)
            intent.putExtra(key, `val`)
        else if (`val` is Array<*>)
            intent.putExtra(key, `val`)
        else if (`val` is String)
            intent.putExtra(key, `val`)
        else if (`val` is Int)
            intent.putExtra(key, `val`)
        else if (`val` is Float)
            intent.putExtra(key, `val`)
        else if (`val` is Long)
            intent.putExtra(key, `val`)
        else if (`val` is Double)
            intent.putExtra(key, `val`)
//        else if (`val` is Array<String>)
//            intent.putExtra(key, `val`)
//        else if (`val` is Array<Int>)
//            intent.putExtra(key, `val`)
//        else if (`val` is Array<Long>)
//            intent.putExtra(key, `val`)
//        else if (`val` is Array<Double>)
//            intent.putExtra(key, `val`)
//        else if (`val` is Array<Float>)
//            intent.putExtra(key, `val`)
    }

    fun getChannel(context: Context): String {
        return getMetaData(context, "UMENG_CHANNEL")
    }

    fun getMetaData(context: Context, key: String): String {
        val appInfo: ApplicationInfo?
        var metaData = "website"
        try {
            appInfo = context.packageManager
                    .getApplicationInfo(context.packageName,
                            PackageManager.GET_META_DATA)
            appInfo?.let {
                metaData = it.metaData.getString(key)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return metaData

    }

}