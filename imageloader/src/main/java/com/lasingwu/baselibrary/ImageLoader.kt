package com.lasingwu.baselibrary

import android.content.Context
import android.view.View
import java.util.*

object ImageLoader {
    private var imageloaderMap = HashMap<LoaderEnum, IImageLoaderstrategy>()
    private var curLoader: LoaderEnum? = null
    fun showImage(options: ImageLoaderOptions) {
        curLoader?.let {
            if (getLoaderstrategy(it) != null) {
                getLoaderstrategy(it)?.showImage(options)
            }
        }
    }

    fun showImage(container: View, url: String) {
        showImage(getDefaultOptions(container, url))
    }

    /*
     *   可创建默认的Options设置，假如不需要使用ImageView ，
     *    请自行new一个Imageview传入即可
     *  内部只需要获取Context
     */
    fun getDefaultOptions(container: View, url: String): ImageLoaderOptions {
        return ImageLoaderOptions.Builder(container, url).isCrossFade(true).build()
    }

    fun showImage(options: ImageLoaderOptions, loaderEnum: LoaderEnum) {
        if (getLoaderstrategy(loaderEnum) != null) {
            getLoaderstrategy(loaderEnum)?.showImage(options)
        }
    }

    fun hideImage(view: View, visiable: Int) {
        curLoader?.let {
            if (getLoaderstrategy(it) != null) {
                getLoaderstrategy(it)?.hideImage(view, visiable)
            }
        }

    }


    fun cleanMemory(context: Context) {
        getLoaderstrategy(curLoader!!)?.cleanMemory(context)
    }

    fun pause(context: Context) {
        curLoader?.let {
            if (getLoaderstrategy(it) != null) {
                getLoaderstrategy(it)?.pause(context)
            }
        }

    }

    fun resume(context: Context) {
        curLoader?.let {
            if (getLoaderstrategy(it) != null) {
                getLoaderstrategy(it)?.resume(context)
            }
        }

    }

    fun setCurImageLoader(loader: LoaderEnum) {
        curLoader = loader
    }

    // 在application的oncreate中初始化
    fun init(context: Context, config: ImageLoaderConfig) {
        imageloaderMap = config.imageloaderMap
        for ((key, value) in imageloaderMap) {
            value.init(context, config)

            if (curLoader == null) {
                curLoader = key
            }
        }

    }

    private fun getLoaderstrategy(loaderEnum: LoaderEnum): IImageLoaderstrategy? {
        return imageloaderMap[loaderEnum]
    }
}