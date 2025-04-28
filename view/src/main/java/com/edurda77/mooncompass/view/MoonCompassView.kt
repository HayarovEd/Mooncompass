package com.edurda77.mooncompass.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.edurda77.mooncompass.common.calculateIndicatorPosition
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.graphics.drawable.toBitmap
import com.edurda77.mooncompass.compose.R


class MoonCompassView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyle: Int = R.style.SolarCompassViewDefaultStyle,
) : View(context, attrs, defaultStyle) {

    private val paint: Paint
    private val indicatorBitmap: Bitmap
    private val indicatorSize: Int

    private var azimuth: Double = Double.NaN
    private var indicatorLeft: Float = 0F
    private var indicatorTop: Float = 0F

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MoonCompassView,
            0,
            defaultStyle,
        )

        paint = Paint()

        val sunDrawable = typedArray
            .getDrawableOrThrow(R.styleable.MoonCompassView_indicator_drawable).mutate().apply {
                setTint(typedArray.getColorOrThrow(R.styleable.MoonCompassView_indicator_color))
            }

        indicatorSize = typedArray.getDimensionPixelSizeOrThrow(
            R.styleable.MoonCompassView_indicator_size,
        )

        indicatorBitmap = sunDrawable.toBitmap(indicatorSize, indicatorSize)

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (azimuth.isNaN()) {
            return
        }
        canvas.drawBitmap(indicatorBitmap, indicatorLeft, indicatorTop, paint)
    }

    fun setSolarAzimuth(azimuth: Double) {
        this.azimuth = azimuth
        val position = calculateIndicatorPosition(
            width = width,
            height = height,
            indicatorSize = indicatorSize,
            azimuth = azimuth,
        )
        indicatorLeft = position.first
        indicatorTop = position.second
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!azimuth.isNaN()) {
            setSolarAzimuth(azimuth)
        }
    }

}
