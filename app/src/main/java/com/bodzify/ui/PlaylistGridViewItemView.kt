package com.bodzify.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class PlaylistGridViewItemView(context: Context?, attrs: AttributeSet?)
    : LinearLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth) //Snap to width
    }
}