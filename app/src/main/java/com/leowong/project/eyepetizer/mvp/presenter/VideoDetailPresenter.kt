package com.leowong.project.eyepetizer.mvp.presenter

import android.app.Activity
import com.agile.android.leo.mvp.BasePresenter
import com.leowong.project.eyepetizer.dataFormat
import com.leowong.project.eyepetizer.managers.NetworkManager
import com.leowong.project.eyepetizer.mvp.contract.VideoDetailContract
import com.leowong.project.eyepetizer.mvp.model.entity.HomeBean
import com.leowong.project.eyepetizer.showToast

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
                        //Todo 待完善
                        (mRootView as Activity).showToast("本次消耗${(mRootView as Activity)
                                .dataFormat(i.urlList[0].size)}流量")
                        break
                    }
                }
            }
        } else {
            mRootView?.setVideo(itemInfo.data.playUrl)
        }

        //设置背景
//        val backgroundUrl = itemInfo.data.cover.blurred + "/thumbnail/${DisplayManager.getScreenHeight()!! - DisplayManager.dip2px(250f)!!}x${DisplayManager.getScreenWidth()}"
//        backgroundUrl.let { mRootView?.setBackground(it) }

        mRootView?.setVideoInfo(itemInfo)


    }

}