package com.android.leo.base.ui.widgets

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.android.leo.base.R


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
