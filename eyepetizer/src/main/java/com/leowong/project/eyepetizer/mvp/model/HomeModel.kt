package com.leowong.project.eyepetizer.mvp.model

import com.agile.android.leo.mvp.BaseModel
import com.leowong.project.eyepetizer.api.ApiManagerService
import com.leowong.project.eyepetizer.managers.RepositoryManager
import com.leowong.project.eyepetizer.mvp.contract.HomeContract
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.android.leo.base.utils.rxjava.SchedulersUtil
import io.reactivex.Observable


class HomeModel : HomeContract.Model, BaseModel() {


    /**
     * 获取首页 Banner 数据
     */

    override fun requestHomeData(num: Int): Observable<HomeBean> {
        return RepositoryManager.obtainRetrofitService(ApiManagerService::class.java).getFirstHomeData(num)

    }

    /**
     * 加载更多
     */
    override fun loadMoreData(url: String): Observable<HomeBean> {

        return RepositoryManager.obtainRetrofitService(ApiManagerService::class.java).getMoreHomeData(url)
                .compose(SchedulersUtil.applyApiSchedulers())
    }

    override fun onDestroy() {
    }


}
