package com.edurda77.mooncompass.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.edurda77.mooncompass.common.calculateIndicatorPosition
import kotlin.math.max
import kotlinx.coroutines.launch


@Composable
fun SolarCompass(
    modifier: Modifier = Modifier.fillMaxSize(),
    azimuth: Double,
    indicatorContent: @Composable () -> Unit,
) {
    Box(modifier) {
        Box(modifier = Modifier.azimuthPosition(azimuth)) {
            indicatorContent()
        }
    }
}

@Composable
fun SolarCompass(
    modifier: Modifier = Modifier.fillMaxSize(),
    azimuth: Double,
    indicatorPainter: Painter = rememberVectorPainter(
        ImageVector.vectorResource(com.edurda77.mooncompass.common.R.drawable.moon_111148),
    ),
    indicatorSize: Dp = 25.dp,
    indicatorColorFilter: ColorFilter = ColorFilter.tint(Color.Gray),
) {
    SolarCompass(azimuth = azimuth, modifier = modifier) {
        Image(
            indicatorPainter,
            contentDescription = null,
            colorFilter = indicatorColorFilter,
            modifier = Modifier.size(indicatorSize),
        )
    }
}

private fun Modifier.azimuthPosition(
    azimuth: Double
) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    val (placeableX, placeableY) = calculateIndicatorPosition(
        width = constraints.maxWidth,
        height = constraints.maxHeight,
        indicatorSize = max(placeable.width, placeable.height),
        azimuth = azimuth,
    )

    layout(placeable.width, placeable.height) {
        placeable.place(placeableX.toInt(), placeableY.toInt())
    }
}

@Preview
@Composable
internal fun SolarCompassPreview() {
    MaterialTheme {
        Surface {
            var azimuth: Double by remember { mutableDoubleStateOf(.0) }
            LaunchedEffect(Unit) {
                launch {
                    animate(
                        initialValue = 0F,
                        targetValue = 360F,
                        initialVelocity = 1F,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing,
                        )
                    ) { value, _ ->
                        azimuth = value.toDouble()
                    }
                }
            }

            SolarCompass(
                azimuth = azimuth,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
