package com.edurda77.mooncompass.azimuth


interface AzumuthToMoonCalculator {
    fun calculateAzimuthFor(latitude: Double, longitude: Double): Double
}