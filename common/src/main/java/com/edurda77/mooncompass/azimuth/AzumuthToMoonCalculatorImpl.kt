package com.edurda77.mooncompass.azimuth

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.TimeZone
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class AzumuthToMoonCalculatorImpl : AzumuthToMoonCalculator {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun calculateAzimuthFor(
        latitude: Double, //широта
        longitude: Double
    ): Double {
        val timeZone = TimeZone.getDefault().rawOffset/(1000*60*60)
        val now = LocalDateTime.now()
        val daysToJd =
            367 * now.year - floor(7 * (now.year + floor((now.monthValue+9) / 12.0)) / 4) + floor(275 * now.monthValue / 9.0) + now.dayOfMonth - 730531.5 + (now.hour + now.minute / 60.0) / 24
        val jd = daysToJd + 2451545
        val jdWithZone = jd - timeZone/24.0
        val i10 = now.hour + now.minute / 60.0 - timeZone
        val periodCentury = (jdWithZone - 2451545.0) / 36525.0
        val l0 = (0.606433 + 1336.855225 * periodCentury) % 1
        val l = 2 * PI * ((0.374897 + 1325.55241 * periodCentury) % 1)
        val ls = 2 * PI * ((0.993133 + 99.997361 * periodCentury) % 1)
        val d = 2 * PI * ((0.827361 + 1236.853086 * periodCentury) % 1)
        val f = 2 * PI * ((0.259086 + 1342.227825 * periodCentury) % 1)
        val dl =
            22640 * sin(l) - 4586 * sin(l - 2 * d) + 2370 * sin(2 * d) + 769 * sin(2 * l) - 668 * sin(
                ls
            ) - 412 * sin(2 * f) - 212 * sin(2 * l - 2 * d) - 206 * sin(l + ls - 2 * d) + 192 * sin(
                l + 2 * d
            ) - 165 * sin(ls - 2 * d) - 125 * sin(d) - 110 * sin(l + ls) + 148 * sin(l - ls) - 55 * sin(
                2 * f - 2 * d
            )
        val s = f + (dl + 412 * sin(2 * f) + 541 * sin(ls)) / 206264.8062
        val h = f - 2 * d
        val n =
            -526 * sin(h) + 44 * sin(l + h) - 31 * sin(-l + h) - 23 * sin(ls + h) + 11 * sin(-ls + h) - 25 * sin(
                -2 * l + f
            ) + 21 * sin(-l + f)
        val lMoon = 2 * PI * ((l0 + dl / 1296000) % 1)
        val bMoon = (18520 * sin(s) + n) / 206264.8062
        val cb = cos(bMoon)
        val x = cb * cos(lMoon)
        val v = cb * sin(lMoon)
        val w = sin(bMoon)
        val y = 0.91748 * v - 0.39778 * w
        val z = 0.39778 * v + 0.91748 * w
        val rho = sqrt(1 - z * z)
        val dec = 360 * atan(z / rho) / (2 * PI)
        val ra =
            if (48 / (2 * PI) * atan(y / (x + rho)) < 0) 48 / (2 * PI) * atan(y / (x + rho)) + 24 else 48 / (2 * PI) * atan(
                y / (x + rho)
            )
        val gst =
            (6.697374558 + 2400.051336 * periodCentury + 0.000025862 * periodCentury * periodCentury + 1.002737909 * i10) % 24
        val lst = (gst + longitude / 15.0).mod(24.0)
        val haDegrees = ((lst - ra) * 15.0).mod(360.0)
        val haRad = haDegrees * PI / 180
        val decRad = dec * PI / 180
        val latRad = latitude * PI / 180
        val alt =
            asin(sin(latRad) * sin(decRad) + cos(latRad) * cos(decRad) * sin(haRad)) * 180 / PI
        val a1 = tan(decRad) * cos(latRad)
        val a2 = sin(latRad) * cos(haRad)
        val a3 = -sin(haRad)
        return (atan2(
            a3,
            a1 - a2,
        )* 180 / PI).mod (360.0)
    }
}