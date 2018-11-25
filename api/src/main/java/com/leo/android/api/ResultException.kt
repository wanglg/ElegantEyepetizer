package com.leo.android.api

/**
 * 自定义请求错误
 * Created by 1 on 2017/10/25.
 */
class ResultException : Exception {
    var errCode: Int? = 0

    constructor(errCode: Int, msg: String) : super(msg) {
        this.errCode = errCode
    }
}
