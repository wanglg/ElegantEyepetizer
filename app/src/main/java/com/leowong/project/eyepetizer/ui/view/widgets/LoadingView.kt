package com.leowong.project.eyepetizer.ui.view.widgets

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.leowong.project.eyepetizer.R


class LoadingView : AppCompatImageView {
    var animationDrawable: AnimationDrawable? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        setImageResource(R.drawable.anim_loading)
        animationDrawable = drawable as AnimationDrawable
        startAnim()
    }

    fun startAnim() {
        animationDrawable?.start()
    }

    fun stopAnim() {
        animationDrawable?.stop()
    }
}
