package com.android.leo.toutiao.mvp.model

import android.text.TextUtils
import com.agile.android.leo.mvp.BaseModel
import com.android.leo.base.BaseApplication
import com.android.leo.base.utils.PreferencesUtil
import com.android.leo.toutiao.Constant
import com.android.leo.toutiao.R
import com.android.leo.toutiao.mvp.contract.TouTiaoContract
import com.android.leo.toutiao.mvp.model.entity.Channel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TouTiaoModel : TouTiaoContract.Model, BaseModel() {

    val mGson = Gson()
    override fun getSelectChannels(): ArrayList<Channel> {
        val mSelectedChannels = ArrayList<Channel>()
        var selectedChannelJson = PreferencesUtil.readString(Constant.SELECTED_CHANNEL_JSON, "")
        if (TextUtils.isEmpty(selectedChannelJson)) {
            //本地没有title
            val channels = BaseApplication.context.getResources().getStringArray(R.array.channel)
            val channelCodes = BaseApplication.context.getResources().getStringArray(R.array.channel_code)
            //默认添加了全部频道
            for (i in channels.indices) {
                val title = channels[i]
                val code = channelCodes[i]
                mSelectedChannels.add(Channel(title, code))
            }
            selectedChannelJson = mGson.toJson(mSelectedChannels)//将集合转换成json字符串
            PreferencesUtil.writePreferences(BaseApplication.context, Constant.SELECTED_CHANNEL_JSON, selectedChannelJson)
        } else {
            //之前添加过
            val selectedChannel = mGson.fromJson<List<Channel>>(selectedChannelJson, object : TypeToken<List<Channel>>() {
            }.type)
            mSelectedChannels.addAll(selectedChannel)
        }
        return mSelectedChannels
    }
}