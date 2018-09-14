package com.leowong.project.eyepetizer.mvp.model

import com.leowong.project.eyepetizer.api.RetrofitManager
import com.leowong.project.eyepetizer.mvp.contract.HomeContract
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.utils.rxjava.SchedulersUtil
import io.reactivex.Observable

/**
 * Created by xuhao on 2017/11/21.
 * desc: 首页精选 model
 */

class HomeModel : HomeContract.Model {


    /**
     * 获取首页 Banner 数据
     */

    override fun requestHomeData(num: Int): Observable<HomeBean> {
        return RetrofitManager.service.getFirstHomeData(num)
                .compose(SchedulersUtil.applyApiSchedulers())
    }

    /**
     * 加载更多
     */
    override fun loadMoreData(url: String): Observable<HomeBean> {

        return RetrofitManager.service.getMoreHomeData(url)
                .compose(SchedulersUtil.applyApiSchedulers())
    }

    override fun onDestroy() {
    }


}
