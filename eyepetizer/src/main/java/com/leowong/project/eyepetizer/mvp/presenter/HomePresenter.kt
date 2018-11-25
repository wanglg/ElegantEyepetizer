package com.leowong.project.eyepetizer.mvp.presenter

import com.agile.android.leo.exception.ApiException
import com.agile.android.leo.mvp.BasePresenter
import com.leo.android.api.ApiSubscriber
import com.leowong.project.eyepetizer.mvp.contract.HomeContract
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.android.leo.base.utils.rxjava.SchedulersUtil
import io.reactivex.disposables.Disposable

class HomePresenter(model: HomeContract.Model, rootView: HomeContract.View) :
        BasePresenter<HomeContract.Model, HomeContract.View>(model, rootView) {

    private var bannerHomeBean: HomeBean? = null
    private var nextPageUrl: String? = null     //加载首页的Banner 数据+一页数据合并后，nextPageUrl没 add
    fun requestHomeData(num: Int) {
        mRootView?.showLoading()
        mModel?.requestHomeData(num)?.flatMap({ homeBean ->
            //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
            val bannerItemList = homeBean.issueList[0].itemList
            bannerHomeBean = homeBean;
            bannerItemList.filter { item ->
                item.type != "video"
            }.forEach { item ->
                //移除 item
                bannerItemList.remove(item)
            }
            mModel?.loadMoreData(homeBean.nextPageUrl)

        })?.compose(SchedulersUtil.applyApiSchedulers())?.subscribe(object : ApiSubscriber<HomeBean>() {
            override fun onFailure(t: ApiException) {
                mRootView?.resultError(t)
                mRootView?.dismissLoading()
            }

            override fun onSubscribe(d: Disposable) {
                addDispose(d)
            }

            override fun onNext(homeBean: HomeBean) {
                mRootView?.apply {
                    dismissLoading()

                    nextPageUrl = homeBean.nextPageUrl
                    //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                    val newBannerItemList = homeBean.issueList[0].itemList

                    newBannerItemList.filter { item ->
                        item.type != "video"
                    }.forEach { item ->
                        //移除 item
                        newBannerItemList.remove(item)
                    }
                    // 重新赋值 Banner 长度
                    bannerHomeBean!!.issueList[0].count = bannerHomeBean!!.issueList[0].itemList.size

                    //赋值过滤后的数据 + banner 数据
                    bannerHomeBean?.issueList!![0].itemList.addAll(newBannerItemList)

                    setHomeData(bannerHomeBean!!)
                }
            }

            override fun onNetWorkError() {
                super.onNetWorkError()
                mRootView?.showNoNetWork()
            }

        })

    }

    fun loadMore() {
        nextPageUrl?.let {
            mModel?.loadMoreData(it)?.compose(SchedulersUtil.applyApiSchedulers())?.subscribe(object : ApiSubscriber<HomeBean>() {
                override fun onFailure(t: ApiException) {
                    mRootView?.resultError(t)
                    mRootView?.loadMoreFailed()
                }

                override fun onSubscribe(d: Disposable) {
                    addDispose(d)
                }

                override fun onNext(homeBean: HomeBean) {
                    mRootView?.apply {
                        //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                        val newItemList = homeBean.issueList[0].itemList

                        newItemList.filter { item ->
                            item.type != "video"
                        }.forEach { item ->
                            //移除 item
                            newItemList.remove(item)
                        }

                        nextPageUrl = homeBean.nextPageUrl
                        setMoreData(newItemList)
                    }
                }

                override fun onNetWorkError() {
                    mRootView?.showNoNetWork()
                }

            })
        }
    }
}