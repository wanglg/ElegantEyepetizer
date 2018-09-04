package com.leowong.project.eyepetizer.ui.view.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.leowong.project.eyepetizer.R

class CustomFontTextView : TextView {
    val boldStyle = 0;
    val lobsterStyle = 1;
    var fontStyle: Int? = -1

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context?.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView, defStyleAttr, 0);
        fontStyle = a?.getInteger(R.styleable.CustomFontTextView_fontName, -1)
        if (fontStyle == boldStyle) {
            val textTypeface = Typeface.createFromAsset(context?.assets, "fonts/DIN-Condensed-Bold.ttf")
            typeface = textTypeface
        } else if (fontStyle == lobsterStyle) {
            val textTypeface = Typeface.createFromAsset(context?.assets, "fonts/Lobster.otf")
            typeface = textTypeface
        }
        a?.recycle()
    }


}