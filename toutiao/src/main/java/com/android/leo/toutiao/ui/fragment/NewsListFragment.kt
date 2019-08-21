package com.android.leo.toutiao.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.agile.android.leo.exception.ApiException
import com.agile.android.leo.utils.ListUtils
import com.android.leo.base.showToast
import com.android.leo.base.ui.fragments.BaseFragment
import com.android.leo.toutiao.Constant
import com.android.leo.toutiao.R
import com.android.leo.toutiao.TouTiaoApp
import com.android.leo.toutiao.mvp.contract.NewsListContract
import com.android.leo.toutiao.mvp.model.NewsListModel
import com.android.leo.toutiao.mvp.model.entity.News
import com.android.leo.toutiao.mvp.presenter.NewsListPresenter
import com.android.leo.toutiao.ui.adapter.NewsListAdapter
import com.android.leo.toutiao.ui.adapter.entity.NewsFeedMultipleEntity
import com.android.leo.toutiao.ui.widget.VideoFeedItemController
import com.android.leo.toutiao.utils.VideoPathParser
import com.chad.library.adapter.base.BaseQuickAdapter
import com.leo.android.log.core.LogUtils
import com.leo.android.videoplayer.IjkVideoView
import com.leo.android.videoplayer.PlayerListManager
import com.leo.android.videoplayer.SimpleMediaPlayerListener
import kotlinx.android.synthetic.main.fragment_news_list.*

class NewsListFragment : BaseFragment<NewsListPresenter>(), NewsListContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    private val mNewsList = ArrayList<News>()
    private var mChannelCode: String? = null
    private var isVideoList: Boolean = false
    /**
     * 是否是推荐频道
     */
    private var isRecommendChannel: Boolean = false
    protected var mNewsAdapter: NewsListAdapter? = null
    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
    private val videoListManager by lazy {
        PlayerListManager()
    }

    override fun resultError(exception: ApiException) {
        mRefreshLayout.finishRefresh()
        showToast(exception.message!!)
    }

    override fun showLoading() {
        multipleStatusView?.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView?.showContent()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_news_list
    }

    override fun initPresenter() {
        mPresenter = NewsListPresenter(NewsListModel(), this)
    }

    override fun configViews() {
        LogUtils.w("NewsListFragment configViews")
        multipleStatusView = feedMultipleStatusView
        multipleStatusView?.showLoading()
        mNewsAdapter = NewsListAdapter(mChannelCode!!, ArrayList())
        mNewsAdapter?.setData(mNewsList)
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.adapter = mNewsAdapter
        mNewsAdapter?.setOnLoadMoreListener(this, mRecyclerView)
        mNewsAdapter?.setOnItemClickListener({ adapter, view, position ->
            val news = mNewsList[position]
            LogUtils.d("position->" + position)
        })
        mNewsAdapter?.setOnItemChildClickListener(object : BaseQuickAdapter.OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (view.id == R.id.video_play_img) {
                    val itemView = linearLayoutManager.findViewByPosition(position)
                    if (itemView == null) {
                        return
                    }
                    view.visibility = View.GONE
                    val ijkVideoView = itemView.findViewById<IjkVideoView>(R.id.video_player)
                    val vidoeCover = itemView.findViewById<ImageView>(R.id.vidoeCover)
                    val video_cover_layout = itemView.findViewById<View>(R.id.video_cover_layout)
                    val item_loading_progress = itemView.findViewById<View>(R.id.item_loading_progress)
                    val newsFeedMultipleEntity = adapter.getItem(position) as NewsFeedMultipleEntity
                    item_loading_progress.visibility = View.VISIBLE
                    //解析播放地址
                    addDispose(VideoPathParser.decodePath(newsFeedMultipleEntity.news!!.url).subscribe({
                        ijkVideoView.setVideoPath(it)
                        LogUtils.d("setVideoPath->" + it)
                        videoListManager.setCurrentVideoView(ijkVideoView)
                        ijkVideoView.addMediaPlayerListener(object : SimpleMediaPlayerListener() {
                            override fun startPrepare(uri: Uri?) {
                                super.startPrepare(uri)
                                view.visibility = View.GONE
                                item_loading_progress.visibility = View.VISIBLE
                            }

                            override fun onFirstFrameStart() {
                                super.onFirstFrameStart()
                                video_cover_layout.visibility = View.GONE
                                vidoeCover.visibility = View.GONE
                                item_loading_progress.visibility = View.GONE
                            }

                            override fun stopPlayer(isPlayComplete: Boolean) {
                                super.stopPlayer(isPlayComplete)
                                view.visibility = View.VISIBLE
                                video_cover_layout.visibility = View.VISIBLE
                                vidoeCover.visibility = View.VISIBLE
                                item_loading_progress.visibility = View.GONE
                            }

                        })

                        val control = VideoFeedItemController(activity!!);
                        control.setNew(newsFeedMultipleEntity.news!!)
                        ijkVideoView.attachMediaControl(control)
                        videoListManager.start()
                    }, {
                        showToast(it.message!!)
                        view.visibility = View.VISIBLE
                        item_loading_progress.visibility = View.GONE
                    }, {

                    }))


                }
            }

        })
        if (TextUtils.equals(mChannelCode, TouTiaoApp.context.mChannelCodes[1])) {
            mRecyclerView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewDetachedFromWindow(p0: View) {
                    LogUtils.d("onChildViewDetachedFromWindow")
                    val ijkVideoView = p0.findViewById<IjkVideoView>(R.id.video_player)
                    if (ijkVideoView != null && !ijkVideoView.isFullScreen) {
                        ijkVideoView.release()
                    }
                }

                override fun onChildViewAttachedToWindow(p0: View) {

                }
            })
        }
        mRefreshLayout.setOnRefreshListener(
                {
                    mPresenter?.requestNewsList(mChannelCode!!)
                })
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
        if (TextUtils.equals(mChannelCode, TouTiaoApp.context.mChannelCodes[1])) {
            videoListManager.onPause()
        }
    }

    private fun clickPlayImg() {

    }

    override fun onBack(): Boolean {
        if (isVideoList) {
            if (videoListManager.isFullScreen) {
                videoListManager.toggleFullScreen()
                return true
            }
        }
        return false
    }

    override fun onFragmentResume(isFirst: Boolean, isViewDestroyed: Boolean) {
        super.onFragmentResume(isFirst, isViewDestroyed)
        if (TextUtils.equals(mChannelCode, TouTiaoApp.context.mChannelCodes[1])) {
            videoListManager.onResume()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mChannelCode = arguments?.getString(Constant.CHANNEL_CODE)
        isVideoList = arguments?.getBoolean(Constant.IS_VIDEO_LIST, false)!!
        val channelCodes = resources.getStringArray(R.array.channel_code)
        isRecommendChannel = mChannelCode == channelCodes[0]//是否是推荐频道
    }

    override fun requestData() {
        mChannelCode?.let {
            mPresenter?.requestNewsList(it)
        }
    }

    override fun onLoadMoreRequested() {
        mChannelCode?.let {
            mPresenter?.loadMore(it)
        }
    }

    /**
     * 处理置顶新闻和广告重复
     */
    private fun dealRepeat(newList: MutableList<News>) {
        if (isRecommendChannel && !ListUtils.isEmpty(mNewsList)) {
            //如果是推荐频道并且数据列表已经有数据,处理置顶新闻或广告重复的问题
            mNewsList.removeAt(0)//由于第一条新闻是重复的，移除原有的第一条
            //新闻列表通常第4个是广告,除了第一次有广告，再次获取的都移除广告
            if (newList.size >= 4) {
                val fourthNews = newList[3]
                //如果列表第4个和原有列表第4个新闻都是广告，并且id一致，移除
                if (fourthNews.tag.equals(Constant.ARTICLE_GENRE_AD)) {
                    newList.remove(fourthNews)
                }
            }
        }
    }


    override fun onGetNewsListSuccess(newList: ArrayList<News>, tipInfo: String) {
        mRefreshLayout.finishRefresh()
        //如果是第一次获取数据
        if (ListUtils.isEmpty(mNewsList)) {
            if (ListUtils.isEmpty(newList)) {
                //获取不到数据,显示空布局
                multipleStatusView?.showEmpty()
                return
            }
        }
        if (ListUtils.isEmpty(newList)) {
            //已经获取不到新闻了，处理出现获取不到新闻的情况
            showToast(getString(R.string.no_news_now))
            return
        }
        if (TextUtils.isEmpty(newList[0].title)) {
            //由于汽车、体育等频道第一条属于导航的内容，所以如果第一条没有标题，则移除
            newList.removeAt(0)
        }
        dealRepeat(newList)//处理新闻重复问题
        mNewsList.addAll(0, newList)
        if (isVideoList) {
            videoListManager.release()
        }
        mNewsAdapter?.replaceData(mNewsList)
    }

    override fun addToEndListFailed(e: ApiException) {
        showToast(e.message!!)
        mNewsAdapter?.loadMoreFail()
    }

    override fun addToEndListSuccess(newList: ArrayList<News>) {
        if (ListUtils.isEmpty(newList)) {
            showToast(getString(R.string.no_news_now))
            mNewsAdapter?.loadMoreEnd()
        } else {
            val filterList = ArrayList<News>()
            for (news in newList) {
                if (!mNewsList.contains(news)) {
                    filterList.add(news)
                }
            }
            mNewsList.addAll(filterList)
            mNewsAdapter?.loadMoreComplete()
            mNewsAdapter?.addItemData(filterList)
        }
    }

}