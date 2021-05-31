package com.jinny.videoplayer

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

// 제스처 이벤트랑 터치 이벤트를 받을거임
class CustomMotionLayout(context: Context, attributeSet: AttributeSet? = null) :
    MotionLayout(context, attributeSet) {
    private var motionTouchStarted = false
    private val mainContainerView by lazy { findViewById<View>(R.id.main_containerLayout) }

    // 재사용을 위해 따로 만들어줌줌
    private val hitRect = Rect()

    // init 함수는 이 CustomMotionLayout이 최초로 만들어질 때
    // 불리는 영역
    init {
        // 트랜지션 끝나면 모션 레이아웃을 다시 false로
        setTransitionListener(object : TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                motionTouchStarted = false
            }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    // 터치 이벤트가 메인 컨테이너 뷰 안에서 일어나는지
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // return super.onTouchEvent(event)
        when (event.actionMasked) {
            // 액션 업(손가락을 뗀다) 액션 캔슬일때는 제외하고 보자
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                motionTouchStarted = false
                // 기존에 있는 리턴값을 그냥 리턴
                return super.onTouchEvent(event)
            }
        }
        if (!motionTouchStarted) {
            mainContainerView.getHitRect(hitRect)
            // 이벤트 발생 좌표가 hitRect 구역 안에서 발생한 거니!?
            motionTouchStarted = hitRect.contains(event.x.toInt(), event.y.toInt())
        }

        return super.onTouchEvent(event) && motionTouchStarted
    }

    private val gestureListener by lazy {
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                mainContainerView.getHitRect(hitRect)
                // 메인 컨테이너 뷰 안에서 일어난거닝?
                return hitRect.contains(e1.x.toInt(), e1.y.toInt())
            }
        }
    }

    private val gestureDetector by lazy {
        // 제스처 리스너를 디텍터로 감싸준다.
        GestureDetector(context, gestureListener)
    }

    // gestureDetector를 이용하여
    // 실제로 메인컨테이너 뷰에서 제스처 이벤트가 일어나는지 봄
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
}