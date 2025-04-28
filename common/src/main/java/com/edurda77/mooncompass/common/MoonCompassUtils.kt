package com.edurda77.mooncompass.common

import androidx.annotation.RestrictTo
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
fun calculateIndicatorPosition(
    width: Int,
    height: Int,
    indicatorSize: Int,
    azimuth: Double,
): Pair<Float, Float> {
    val convertedAngleRadians = toRadians(azimuth)

    val halfOfSunSize = indicatorSize / 2F

    val centerX = width / 2F
    val centerY = height / 2F

    val (left, top) =
        if (azimuth in listOf(.0, 90.0, 180.0, 270.0, 360.0)) {
            when (azimuth) {
                .0, 360.0 -> centerX - halfOfSunSize to 0F
                90.0 -> width.toFloat() - indicatorSize to centerY - halfOfSunSize
                180.0 -> centerX - halfOfSunSize to height.toFloat() - indicatorSize
                else -> 0F to centerY - halfOfSunSize
            }
        } else {

            val x2 = centerX + centerX * sin(convertedAngleRadians)
            val y2 = centerY - centerX * cos(convertedAngleRadians)

            val line = Line2D.createBy(centerX.toDouble(), centerY.toDouble(), x2, y2)

            when (azimuth) {
                in 90.0..270.0 -> {
                    val bottomX = line.xFor(height.toDouble())
                    if (bottomX <= width && bottomX >= 0) {
                        line.xFor(height - halfOfSunSize.toDouble())
                            .toFloat() - halfOfSunSize to height - indicatorSize.toFloat()
                    } else if (azimuth < 180) {
                        width - indicatorSize.toFloat() to line.yFor(width - halfOfSunSize.toDouble())
                            .toFloat() - halfOfSunSize
                    } else {
                        0F to line.yFor(halfOfSunSize.toDouble()).toFloat() - halfOfSunSize
                    }
                }

                else -> {
                    val topX = line.xFor(.0)
                    if (topX <= width && topX >= 0) {
                        line.xFor(halfOfSunSize.toDouble()).toFloat() - halfOfSunSize to 0F
                    } else if (azimuth >= 270 && azimuth < 360) {
                        0F to line.yFor(halfOfSunSize.toDouble()).toFloat() - halfOfSunSize
                    } else {
                        width - indicatorSize.toFloat() to line.yFor(width - halfOfSunSize.toDouble())
                            .toFloat() - halfOfSunSize
                    }
                }
            }
        }

    return left to top
}

private class Line2D(val a: Double, val b: Double, val c: Double) {

    fun yFor(x: Double) = -(a * x + c) / b

    fun xFor(y: Double) = -(b * y + c) / a

    companion object {
        fun createBy(x1: Double, y1: Double, x2: Double, y2: Double): Line2D {
            val a = y1 - y2
            val b = x2 - x1
            val c = x1 * y2 - x2 * y1
            return Line2D(a, b, c)
        }
    }
}
