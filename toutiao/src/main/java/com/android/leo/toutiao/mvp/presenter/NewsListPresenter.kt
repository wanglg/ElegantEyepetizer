package com.android.leo.toutiao.mvp.presenter

import com.agile.android.leo.exception.ApiException
import com.agile.android.leo.mvp.BasePresenter
import com.agile.android.leo.utils.ListUtils
import com.android.leo.base.utils.PreferencesUtil
import com.android.leo.base.utils.rxjava.SchedulersUtil
import com.android.leo.toutiao.mvp.contract.NewsListContract
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.mvp.model.response.NewsResponse
import com.google.gson.Gson
import com.leo.android.api.ApiSubscriber
import io.reactivex.disposables.Disposable
import java.util.ArrayList

class NewsListPresenter(model: NewsListContract.Model, rootView: NewsListContract.View) : BasePresenter<NewsListContract.Model, NewsListContract.View>(model, rootView) {
    private var lastTime: Long = 0
    fun requestNewsList(channelCode: String) {
//        lastTime = PreferencesUtil.readLong(channelCode)//读取对应频道下最后一次刷新的时间戳
//        if (lastTime == 0L) {
        //如果为空，则是从来没有刷新过，使用当前时间戳
        lastTime = (System.currentTimeMillis() / 1000)
//        }
        mModel?.getNewsLst(channelCode, lastTime)?.compose(SchedulersUtil.applyApiSchedulers())
                ?.subscribe(object : ApiSubscriber<NewsResponse>() {
                    override fun onFailure(t: ApiException) {
                        mRootView?.dismissLoading()
                        mRootView?.resultError(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        addDispose(d)
                    }

                    override fun onNext(response: NewsResponse) {
                        mRootView?.dismissLoading()
//                        lastTime = System.currentTimeMillis() / 1000
//                        PreferencesUtil.writeValue(channelCode, lastTime)//保存刷新的时间戳
                        val data = response.data
                        val newsList = ArrayList<News>()
                        if (!ListUtils.isEmpty(data)) {
                            val mGson = Gson()
                            for (newsData in data) {
                                val news = mGson.fromJson<News>(newsData.content, News::class.java)
                                newsList.add(news)
                            }
                        }
                        if (newsList.size > 0) {
                            lastTime = newsList[newsList.size - 1].behot_time
                        }
                        mRootView?.onGetNewsListSuccess(newsList, response.tips.display_info)
                    }

                })
    }

    fun loadMore(channelCode: String) {
        mModel?.getNewsLst(channelCode, lastTime)?.compose(SchedulersUtil.applyApiSchedulers())
                ?.subscribe(object : ApiSubscriber<NewsResponse>() {
                    override fun onFailure(t: ApiException) {
                        mRootView?.addToEndListFailed(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        addDispose(d)
                    }

                    override fun onNext(response: NewsResponse) {
//                        lastTime = System.currentTimeMillis() / 1000
//                        PreferencesUtil.writeValue(channelCode, lastTime)//保存刷新的时间戳
                        val data = response.data
                        val newsList = ArrayList<News>()
                        if (!ListUtils.isEmpty(data)) {
                            val mGson = Gson()
                            for (newsData in data) {
                                val news = mGson.fromJson<News>(newsData.content, News::class.java)
                                newsList.add(news)
                            }
                        }
                        mRootView?.addToEndListSuccess(newsList)
                    }

                })
    }
}