package com.tagreader

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class PacManProgress(context: Context?, attrs: AttributeSet?) : BasePacMan(context, attrs) {

    var percentvalue = 1f
    var subscription: Disposable? = null
    var increase = true

    init {
        startTimer()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

    private fun stop() {
        subscription?.let {
            it.dispose()
        }
    }

    override fun onVisibilityChanged(changedView: View?, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        when (visibility) {
            INVISIBLE -> stop()
            VISIBLE -> startTimer()
        }
    }

    private fun startTimer() {
        stop()
        subscription = Observable
                .just(0)
                .delay(10, TimeUnit.MILLISECONDS)
                .repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ _ ->
                    processPercentValue()
                    changeDirection()
                    invalidate()
                })
    }

    private fun changeDirection() {
        when {
            percentvalue >= 100 -> increase = false
            percentvalue <= 0 -> increase = true
        }
    }

    private fun processPercentValue() {
        when (increase) {
            true -> percentvalue++
            false -> percentvalue--
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val center = width / 2f
        canvas?.let {
            drawPart(it, center, center, percentvalue)
        }
    }

    override fun Canvas.getRotation(canvas: Canvas): Float {
        return when (increase) {
            true -> -90f
            false -> {
                flipCanvas(canvas)
                90f
            }
        }
    }

    override fun getPercent(): Float = percentvalue
}