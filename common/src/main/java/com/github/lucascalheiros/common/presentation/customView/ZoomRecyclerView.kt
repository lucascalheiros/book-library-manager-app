package com.github.lucascalheiros.common.presentation.customView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.recyclerview.widget.RecyclerView


class ZoomRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    companion object {
        const val BASE_SCALE = 1f
    }

    var scale = BASE_SCALE
    var mPivotX = 0f
    var mPivotY = 0f

    var mAnchorX = 0f
    var mAnchorY = 0f

    private val scaleDetector =
        ScaleGestureDetector(context, object : SimpleOnScaleGestureListener() {
            var startingSpan = 0f
            var startFocusX = 0f
            var startFocusY = 0f
            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                startingSpan = detector.scaleFactor
                startFocusX = detector.focusX
                startFocusY = detector.focusY
                return true
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scale(
                    detector.scaleFactor,
                    startFocusX,
                    startFocusY
                )
                return true
            }
        })

    private val doubleTapDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onDoubleTap(e: MotionEvent): Boolean {
                doubleTapScaling(e.x, e.y)
                return super.onDoubleTap(e)
            }
        })

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        doubleTapDetector.onTouchEvent(event)

        event.setLocation(scrollX + event.x, (scrollY + event.y) / scale)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mAnchorX = event.x
                mAnchorY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val diffX = mAnchorX - event.x
                val diffY = mAnchorY - event.y
                mPivotX = (mPivotX + diffX).coerceIn(0f..width.toFloat())
                mAnchorX = event.x
                mAnchorY = event.y

                if (!canScrollVertically(1) || !canScrollVertically(-1)) {
                    mPivotY = (mPivotY + diffY).coerceIn(0f..height.toFloat())
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()
        canvas.scale(scale, scale, mPivotX, mPivotY)
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    fun scale(scaleFactor: Float, pivotX: Float, pivotY: Float) {
        scale *= scaleFactor
        mPivotX = pivotX
        mPivotY = pivotY
        this.invalidate()
    }

    fun doubleTapScaling(x: Float?, y: Float?) {
        scale = when {
            scale > BASE_SCALE -> BASE_SCALE
            scale == BASE_SCALE -> BASE_SCALE + 1
            else -> BASE_SCALE
        }
        mPivotX = x ?: mPivotX
        mPivotY = y ?: mPivotY
        this.invalidate()
    }
}