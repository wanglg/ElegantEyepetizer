package com.leowong.project.eyepetizer.managers

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.android.leo.base.BaseApplication
import com.leowong.project.eyepetizer.constants.GlobalConstants
import com.leowong.project.eyepetizer.receiver.NetChangeReceiver

/**
 * User: wanglg
 * Date: 2018-05-30
 * Time: 11:10
 * FIXME
 */
class NetworkManager private constructor() {
    var networkType: Int = 0
    var networkReceiver: NetChangeReceiver? = null

    private object Holder {
        val INSTANCE = NetworkManager()
    }

    companion object {
        val instance: NetworkManager by lazy { Holder.INSTANCE }
    }

    init {
        val manager = BaseApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = manager!!.activeNetworkInfo
        if (networkInfo != null && networkInfo.isAvailable) {
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                networkType = GlobalConstants.NetworkType.WIFI_NETWORK
            } else {
                networkType = GlobalConstants.NetworkType.MOBILE_NETWORK
            }
        } else {
            networkType = GlobalConstants.NetworkType.NO_NETWORK
        }
        networkReceiver = NetChangeReceiver()
    }

    fun isNetWorkConnected(): Boolean {
        return networkType != GlobalConstants.NetworkType.NO_NETWORK
    }

    fun isWifiConnected(): Boolean {
        return networkType == GlobalConstants.NetworkType.WIFI_NETWORK
    }

    fun isMobileNetWorkConnected(): Boolean {
        return networkType == GlobalConstants.NetworkType.MOBILE_NETWORK
    }

    /**
     * 注册网络监听
     */
    fun registerReceiver(context: Context) {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, intentFilter)
    }

    /**
     * 取消网络监听
     */
    fun unregisterReceiver(context: Context) {
        context.unregisterReceiver(networkReceiver)
    }
}