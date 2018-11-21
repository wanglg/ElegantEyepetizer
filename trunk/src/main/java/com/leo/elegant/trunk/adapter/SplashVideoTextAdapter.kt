package com.leo.elegant.trunk.adapter

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.leo.elegant.trunk.R

class SplashVideoTextAdapter : PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return 4
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_splash_video, null)
        val text_cn = view.findViewById<TextView>(R.id.splash_tx_cn)
        val text_en = view.findViewById<TextView>(R.id.splash_tx_en)
        if (position == 0) {
            text_cn.setText(R.string.splash_guide1_chinese)
            text_en.setText(R.string.splash_guide1_english)
        }
        if (position == 1) {
            text_cn.setText(R.string.splash_guide2_chinese)
            text_en.setText(R.string.splash_guide2_english)
        }
        if (position == 2) {
            text_cn.setText(R.string.splash_guide3_chinese)
            text_en.setText(R.string.splash_guide3_english)
        }
        if (position == 3) {
            text_cn.setText(R.string.splash_guide4_chinese)
            text_en.setText(R.string.splash_guide4_english)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (`object` is View) {
            container.removeView(`object`)
        }
    }

}