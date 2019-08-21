package  com.leo.android.api

import com.agile.android.leo.exception.ApiException
import com.google.gson.JsonParseException
import com.leo.android.log.core.LogUtils
import io.reactivex.Observer
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeoutException

/**
 * User: wanglg
 * Date: 2018-05-11
 * Time: 15:28
 */
abstract class ApiSubscriber<T> : Observer<T> {
    override fun onError(t: Throwable) {
        onFailure(wrapException(t))
        if (t is ConnectException ||
                t is SocketTimeoutException ||
                t is TimeoutException ||
                t is UnknownHostException) {
            onNetWorkError()
        }
    }


    /**
     * 所有的失败情况，自定义了异常信息
     */
    abstract fun onFailure(t: ApiException)

    /**
     * 网络异常情况单独提取出来，可以在此做相关处理
     */
    open fun onNetWorkError() {

    }

    override fun onComplete() {
    }

    fun wrapException(e: Throwable): ApiException {
        e.printStackTrace()
        val errorCode: Int?
        val errorMsg: String
        if (e is ConnectException ||
                e is SocketTimeoutException ||
                e is TimeoutException ||
                e is UnknownHostException) {//网络超时
            LogUtils.e("TAG", "网络连接异常: " + e.message)
            errorMsg = "网络连接异常"
            errorCode = ErrorCode.NETWORK_ERROR
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException) {   //均视为解析错误
            LogUtils.e("TAG", "数据解析异常: " + e.message)
            errorMsg = "数据解析异常"
            errorCode = ErrorCode.SERVER_ERROR
        } else if (e is ResultException) {//服务器返回的错误信息
            errorMsg = e.message!!
            errorCode = e.errCode
        } else if (e is IllegalArgumentException) {
            errorMsg = "参数错误"
            errorCode = ErrorCode.SERVER_ERROR
        } else {//未知错误
            try {
                LogUtils.e("TAG", "错误: " + e.message)
            } catch (e1: Exception) {
                LogUtils.e("TAG", "未知错误Debug调试 ")
            }

            errorMsg = "未知错误，可能抛锚了吧~"
            errorCode = ErrorCode.UNKNOWN_ERROR
        }
        return ApiException(errorCode!!, errorMsg)
    }
}