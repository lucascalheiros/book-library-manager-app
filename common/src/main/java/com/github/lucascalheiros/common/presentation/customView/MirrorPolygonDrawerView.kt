package com.github.lucascalheiros.common.presentation.customView

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.res.getFloatOrThrow
import com.github.lucascalheiros.common.R


fun TypedArray.toFloatList(): List<Float> {
    return (0 until length()).map { getFloatOrThrow(it) }
}

class MirrorPolygonDrawerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @ColorInt
    var color: Int = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }
    var listPaths: List<Pair<Float, Float>> = listOf()
        set(value) {
            field = value
            invalidate()
        }
    var radius: Int = 500
        set(value) {
            field = value
            invalidate()
        }

    private val polygonPoints = mutableListOf<Pair<Float, Float>>()

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MirrorPolygonDrawerView,
            defStyleAttr,
            0
        )
        color = typedArray.getColor(R.styleable.MirrorPolygonDrawerView_color, color)
        radius = typedArray.getInteger(R.styleable.MirrorPolygonDrawerView_radius, radius)
        listPaths = try {
            val xList = resources.obtainTypedArray(
                typedArray.getResourceId(
                    R.styleable.MirrorPolygonDrawerView_points_x,
                    0
                )
            ).toFloatList()
            val yList = resources.obtainTypedArray(
                typedArray.getResourceId(
                    R.styleable.MirrorPolygonDrawerView_points_y,
                    0
                )
            ).toFloatList()
            if (xList.size == yList.size) {
                xList.indices.map { xList[it] to yList[it] }
            } else {
                listPaths
            }
        } catch (e: Exception) {
            listPaths
        }

        typedArray.recycle()
    }

    override fun onSizeChanged(xNew: Int, yNew: Int, xOld: Int, yOld: Int) {
        super.onSizeChanged(xNew, yNew, xOld, yOld)
        (listPaths.map {
            xNew.toFloat() * (1 - it.first) to
                    yNew.toFloat() * it.second

        } + listPaths.asReversed().map {
            xNew.toFloat() * it.first to
                    yNew.toFloat() * it.second

        }).let {
            polygonPoints.clear()
            polygonPoints.addAll(it)
        }
    }

    override fun draw(canvas: Canvas) {
        val corEffect = CornerPathEffect(radius.toFloat())
        val paint = Paint()
        paint.color = color
        paint.pathEffect = corEffect

        val initial = polygonPoints.last()

        val path = Path()
        path.moveTo(
            initial.first,
            initial.second
        )

        polygonPoints.forEach {
            path.lineTo(
                it.first,
                it.second
            )
        }

        path.close()
        canvas.drawPath(path, paint)
    }
}