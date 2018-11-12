package com.agile.android.leo.mvp

import com.agile.android.leo.integration.IRepositoryManager

/**
 * User: wanglg
 * Date: 2018-05-02
 * Time: 15:29
 * FIXME
 */
open class BaseModel : IModel {
    protected var mRepositoryManager: IRepositoryManager? = null//用于管理网络请求层, 以及数据缓存层

//    protected constructor(repositoryManager: IRepositoryManager) {
//        this.mRepositoryManager = repositoryManager
//    }

    override fun onDestroy() {
        mRepositoryManager = null
    }
}