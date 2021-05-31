package com.jinny.videoplayer

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

class CustomMotionLayout(context: Context, attributeSet: AttributeSet? = null) :
    MotionLayout(context, attributeSet) {
    private val motionTouchStarted = false
    private val mainContainerView by lazy { findViewById<View>(R.id.main_containerLayout) }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
        when(event.Action)
    }
}