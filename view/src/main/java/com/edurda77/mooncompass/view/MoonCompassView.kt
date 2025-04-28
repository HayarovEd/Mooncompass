package com.edurda77.mooncompass.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.graphics.drawable.toBitmap

/**
 * View that displays solar indicator on its edges based on the azimuth.
 *
 * Use [setSolarAzimuth] to provide azimuth for the View
 */
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
            R.styleable.SolarCompassView,
            0,
            defaultStyle,
        )

        paint = Paint()

        val sunDrawable = typedArray
            .getDrawableOrThrow(R.styleable.SolarCompassView_indicator_drawable).mutate().apply {
                setTint(typedArray.getColorOrThrow(R.styleable.SolarCompassView_indicator_color))
            }

        indicatorSize = typedArray.getDimensionPixelSizeOrThrow(
            R.styleable.SolarCompassView_indicator_size,
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

    /**
     * Set current solar azimuth
     *
     * @param azimuth azimuth in degrees
     */
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
