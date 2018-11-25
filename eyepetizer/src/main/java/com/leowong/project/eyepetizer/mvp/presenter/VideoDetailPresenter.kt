package com.leowong.project.eyepetizer.mvp.presenter

import android.app.Activity
import com.agile.android.leo.exception.ApiException
import com.agile.android.leo.mvp.BasePresenter
import com.android.leo.base.*
import com.android.leo.base.manager.NetworkManager
import com.leo.android.api.ApiSubscriber
import com.leowong.project.eyepetizer.mvp.contract.VideoDetailContract
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.android.leo.base.utils.rxjava.SchedulersUtil
import com.leowong.project.eyepetizer.R
import io.reactivex.disposables.Disposable

class VideoDetailPresenter(model: VideoDetailContract.Model, rootView: VideoDetailContract.View) :
        BasePresenter<VideoDetailContract.Model, VideoDetailContract.View>(model, rootView) {
    fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {

        val playInfo = itemInfo.data?.playInfo

        val netType = NetworkManager.instance.isWifiConnected()
        if (playInfo!!.size > 1) {
            // 当前网络是 Wifi环境下选择高清的视频
            if (netType) {
                for (i in playInfo) {
                    if (i.type == "high") {
                        val playUrl = i.url
                        mRootView?.setVideo(playUrl)
                        break
                    }
                }
            } else {
                //否则就选标清的视频
                for (i in playInfo) {
                    if (i.type == "normal") {
                        val playUrl = i.url
                        mRootView?.setVideo(playUrl)
                        if(NetworkManager.instance.isMobileNetWorkConnected()){
                            (mRootView as Activity).showToast("本次消耗${(mRootView as Activity)
                                    .dataFormat(i.urlList[0].size)}流量")
                        }
                        break
                    }
                }
            }
        } else {
            mRootView?.setVideo(itemInfo.data.playUrl)
        }

        //设置背景
        val backgroundUrl = itemInfo.data.cover.blurred + "/thumbnail/${getScreenHeight(BaseApplication.context) -
                BaseApplication.context.resources.getDimensionPixelOffset(R.dimen.detail_video_height)}x${getScreenWidth(BaseApplication.context)}"
        backgroundUrl.let { mRootView?.setBackground(it) }

        mRootView?.setVideoInfo(itemInfo)


    }


    /**
     * 请求相关的视频数据
     */
    fun requestRelatedVideo(id: Long) {
        mRootView?.showLoading()
        mModel?.requestRelatedVideo(id)?.compose(SchedulersUtil.applyApiSchedulers())
                ?.subscribe(object : ApiSubscriber<HomeBean.Issue>() {
                    override fun onFailure(t: ApiException) {
                        mRootView?.dismissLoading()
                        mRootView?.resultError(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        addDispose(d)
                    }


                    override fun onNext(t: HomeBean.Issue) {
                        mRootView?.dismissLoading()
                        mRootView?.setRecentRelatedVideo(t.itemList)
                    }

                })

    }


}

