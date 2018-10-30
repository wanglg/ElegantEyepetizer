package  com.leowong.project.eyepetizer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.agile.android.leo.utils.LogUtils
import com.leowong.project.eyepetizer.constants.GlobalConstants
import com.leowong.project.eyepetizer.events.NetChangeEvent
import com.leowong.project.eyepetizer.managers.NetworkManager
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
                    if (NetworkManager.instance.networkType != GlobalConstants.NetworkType.WIFI_NETWORK) {
                        NetworkManager.instance.networkType = GlobalConstants.NetworkType.WIFI_NETWORK
                        EventBus.getDefault().post(NetChangeEvent(true))
                        LogUtils.d(TAG, "Connected to WIFI !!!")
                    } else {
                        LogUtils.d(TAG, "Connected to WIFI !!!")
                    }
                } else {
                    if (NetworkManager.instance.networkType != GlobalConstants.NetworkType.MOBILE_NETWORK) {
                        NetworkManager.instance.networkType = GlobalConstants.NetworkType.MOBILE_NETWORK
                        EventBus.getDefault().post(NetChangeEvent(true))
                        LogUtils.d(TAG, "Connected to Mobile !!!")
                    } else {
                        LogUtils.d(TAG, "Connected to Mobile !!!")
                    }
                }
            } else {
                LogUtils.d(TAG, "No Network !!!")
                NetworkManager.instance.networkType = GlobalConstants.NetworkType.NO_NETWORK
                EventBus.getDefault().post(NetChangeEvent(false))
            }
        }

    }
}