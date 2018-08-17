package com.leowong.project.eyepetizer.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 请求回调 BASE
 * Created by wentao on 17/10/24.
 */

class BaseResponse<T> : Serializable {
    @SerializedName("code")
    var code: String? = null

    @SerializedName("message")
    var message: String? = ""

    @SerializedName("data")
    var data: T? = null

    var isSuccess: Boolean = false
        get() = ServiceCode.RESPONSE_SUCCESS.equals(code)
}
