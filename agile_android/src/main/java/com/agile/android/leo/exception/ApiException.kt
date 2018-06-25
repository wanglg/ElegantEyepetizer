package com.agile.android.leo.exception

/**
 * User: wanglg
 * Date: 2018-05-14
 * Time: 10:59
 * FIXME
 */
class ApiException : RuntimeException {
    var errCode: Int = 0

    constructor(errCode: Int, msg: String) : super(msg) {
        this.errCode = errCode
    }
}