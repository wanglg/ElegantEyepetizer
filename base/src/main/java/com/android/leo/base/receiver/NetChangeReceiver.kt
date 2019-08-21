package  com.android.leo.base.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.android.leo.base.events.NetChangeEvent
import com.android.leo.base.manager.NetworkManager
import com.android.leo.base.manager.NetworkManager.Companion.MOBILE_NETWORK
import com.android.leo.base.manager.NetworkManager.Companion.NO_NETWORK
import com.android.leo.base.manager.NetworkManager.Companion.WIFI_NETWORK
import com.leo.android.log.core.LogUtils
import org.greenrobot.eventbus.EventBus

/**
 * User: wanglg
 * Date: 2018-05-30
 * Time: 11:38
 * FIXME
 */
class NetChangeReceiver : BroadcastReceiver() {
    val TAG = "NetChangeReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val manager = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = manager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isAvailable()) {
                if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    if (NetworkManager.instance.networkType != WIFI_NETWORK) {
                        NetworkManager.instance.networkType = WIFI_NETWORK
                        EventBus.getDefault().post(NetChangeEvent(true))
                        LogUtils.d(TAG, "Connected to WIFI !!!")
                    } else {
                        LogUtils.d(TAG, "Connected to WIFI !!!")
                    }
                } else {
                    if (NetworkManager.instance.networkType != MOBILE_NETWORK) {
                        NetworkManager.instance.networkType = MOBILE_NETWORK
                        EventBus.getDefault().post(NetChangeEvent(true))
                        LogUtils.d(TAG, "Connected to Mobile !!!")
                    } else {
                        LogUtils.d(TAG, "Connected to Mobile !!!")
                    }
                }
            } else {
                LogUtils.d(TAG, "No Network !!!")
                NetworkManager.instance.networkType = NO_NETWORK
                EventBus.getDefault().post(NetChangeEvent(false))
            }
        }

    }
}