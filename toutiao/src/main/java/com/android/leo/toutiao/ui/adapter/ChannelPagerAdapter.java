package com.android.leo.toutiao.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.leo.toutiao.mvp.model.entity.Channel;
import com.android.leo.toutiao.ui.fragment.NewsListFragment;

import java.util.ArrayList;
import java.util.List;


public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private List<NewsListFragment> mFragments;
    private List<Channel> mChannels;

    public ChannelPagerAdapter(List<NewsListFragment> fragmentList, List<Channel> channelList, FragmentManager fm) {
        super(fm);
        mFragments = fragmentList == null ? new ArrayList<NewsListFragment>() : fragmentList;
        mChannels = channelList == null ? new ArrayList<Channel>() : channelList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).title;
    }

}
