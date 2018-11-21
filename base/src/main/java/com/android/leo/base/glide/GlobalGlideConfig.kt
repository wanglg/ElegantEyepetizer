package com.android.leo.base.glide

import android.content.Context
import android.os.Build
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions


/**
 * glide 4.0 依赖 不可删除 by wentao
 */
@GlideModule
class GlobalGlideConfig : AppGlideModule() {
    var diskSize = 1024 * 1024 * 100L
    var memorySize = Runtime.getRuntime().maxMemory().toInt() / 8L  // 取1/8最大内存作为最大缓存
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        // 定义缓存大小和位置
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskSize))  //内存中
        //data/user/0/com.playablestudio.caowt/cache
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, context.cacheDir.path, diskSize * 10)) //sd卡中

        // 默认内存和图片池大小
        val calculator = MemorySizeCalculator.Builder(context)
        val defaultMemoryCacheSize = calculator.build().memoryCacheSize // 默认内存大小
        val defaultBitmapPoolSize = calculator.build().bitmapPoolSize // 默认图片池大小
        builder.setMemoryCache(LruResourceCache(defaultMemoryCacheSize.toLong())) // 该两句无需设置，是默认的
        builder.setBitmapPool(LruBitmapPool(defaultBitmapPoolSize.toLong()))
//
//        // 自定义内存和图片池大小
        builder.setMemoryCache(LruResourceCache(memorySize))
        builder.setBitmapPool(LruBitmapPool(memorySize))
//
//        // 定义图片格式
////        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
        //disallowHardwareConfig 解决8.0系统报错 Software rendering doesn't support hardware bitmaps
        val requestOptions = RequestOptions()
        requestOptions.format(DecodeFormat.PREFER_RGB_565)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestOptions.disallowHardwareConfig()
        }
        builder.setDefaultRequestOptions(requestOptions)
    }
}