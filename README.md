
All information and source code are provided AS-IS, without express 
or implied warranties. Use of the source code or parts of it is at
your sole discretion and risk. Edward Khayarov takes reasonable measures
to ensure the relevance of the information posted in this repository,
but it does not assume responsibility for maintaining or updating
this repository or its parts outside the framework established by the 
company independently and without notifying third parties.
# Moon compass library

This library provides implementations of the UI elements (both View based and Compose) for the
moon compass.

The library calculates moon azimuth (angle between north and sun) and displays and indicator for 
the moon on the screen.

# Usage

The library provides implementation for the moon azimuth calculation algorithm. Usage:

```kotlin
// Creation default implementation. `create` has optional parameter - timeZone which uses 
// TimeZone.getDefault() by default 
val azimuthCalculator: AzimuthToMoonCalculator = AzimuthToMoonCalculator.create()
// Calculate azimuth for the given latitude and longitude 
val azimuth = azimuthCalculator.calculateAzimuthFor(
    latitude = latitude,
    longitude = longitude,
)
```

## View

The library provides View implementation of the solar compass

Add `MoonCompassView` to the layout:
```xml
<com.edurda77.mooncompass.view.MoonCompassView
    android:id="@+id/compass"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Set azimuth to the view
```kotlin
binding.compass.setMoonAzimuth(
    azimuthCalculator.calculateAzimuthFor(
        latitude = latitude,
        longitude = longitude,
    ),
)
```

The View provides ability to configure indicator using attributes. Complete example:
```xml
<com.edurda77.mooncompass.view.MoonCompassView
    android:id="@+id/compass"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:indicator_color="@color/moonCompassIndicatorColor"
    app:indicator_drawable="@drawable/moon_111148"
    app:indicator_size="25dp" />
```

## Compose

Call SolarCompass with calculated azimuth  
```kotlin
@Composable
fun content() {
    MoonCompass(azimuth, modifier = Modifier.fillMaxSize())
}
```

The composable provides ability to configure indicator using parameters. Complete example:
```kotlin
@Composable
fun content(azimuth: Double) {
    MoonCompass(
        azimuth,
        indicatorPainter = rememberVectorPainter(
            ImageVector.vectorResource(R.drawable.moon_111148),
        ),
        indicatorSize = 25.dp,
        indicatorColorFilter = ColorFilter.tint(Color.Gray),
        modifier = Modifier.fillMaxSize(),
    )
}
```
The composable provides ability use custom indicator. Example:
```kotlin
@Composable
fun content(azimuth: Double) {
    MoonCompass(azimuth = azimuth, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.size(25.dp)
                .background(Color.Gray)
        )
    }
}
```

# Artifacts


# Integration

Steps:
* Configure maven repository in gradle.  
* Add desired artifact dependency.



# build.gradle
```groovy
dependencies {
    implementation("com.edurda77.solarcompass:view:{version}")
}
