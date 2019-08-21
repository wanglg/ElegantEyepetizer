package com.android.leo.base.manager

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.android.leo.base.BaseApplication
import com.android.leo.base.receiver.NetChangeReceiver

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
        const val NO_NETWORK: Int = 0
        const val MOBILE_NETWORK: Int = 1
        const val WIFI_NETWORK: Int = 2
        val instance: NetworkManager by lazy { Holder.INSTANCE }
    }

    init {
        val manager = BaseApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = manager!!.activeNetworkInfo
        if (networkInfo != null && networkInfo.isAvailable) {
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                networkType = WIFI_NETWORK
            } else {
                networkType = MOBILE_NETWORK
            }
        } else {
            networkType = NO_NETWORK
        }
        networkReceiver = NetChangeReceiver()
    }

    fun isNetWorkConnected(): Boolean {
        return networkType != NO_NETWORK
    }

    fun isWifiConnected(): Boolean {
        return networkType == WIFI_NETWORK
    }

    fun isMobileNetWorkConnected(): Boolean {
        return networkType == MOBILE_NETWORK
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