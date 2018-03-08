package com.tagreader

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

abstract class BasePacMan(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        val PADDING = 4F
        val STROKE_WIDTH = 4f
        val DEGREE_STEP = 3.6F
    }

    inline fun saveRestore(canvas: Canvas, code: () -> Unit) {
        with (canvas) {
            save()
            code()
            restore()
        }
    }

    val paint = Paint().apply {
        isAntiAlias = true
        color = context?.getColor(R.color.colorPrimaryDark) ?: Color.RED
        strokeWidth = STROKE_WIDTH
    }

    val paintDiff = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val center = width / 2f
        canvas?.let {
            drawPart(it, center, center, getPercent())
        }
    }

    fun drawPart(canvas: Canvas, centerX: Float, centerY: Float, percent: Float) {
        paint.style = Paint.Style.FILL
        with(canvas) {
            saveRestore(this) {
                rotatePart(this, centerX, centerY)
                drawArc(createArcRectangle(), 0f, percent * DEGREE_STEP, true, paint)
                canvas.drawCircle(centerX, centerY, centerX - 12f, paintDiff)
            }
        }
    }

    private fun Canvas.rotatePart(canvas: Canvas, centerX: Float, centerY: Float) {
        val rotation = getRotation(canvas)
        rotate(rotation, centerX, centerY)
    }

    abstract fun Canvas.getRotation(canvas: Canvas): Float
    abstract fun getPercent(): Float

    fun Canvas.flipCanvas(canvas: Canvas) {
        val halfWidth = width / 2f
        val halfHeight = height / 2f
        canvas.scale(1f, -1f, halfWidth, halfHeight)
    }

    private fun Canvas.createArcRectangle(): RectF {
        return RectF(PADDING, PADDING, (this.width - PADDING), (this.height - PADDING))
    }
}