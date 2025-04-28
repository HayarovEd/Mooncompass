plugins {
    id("com.edurda77.android.library")
    id("com.edurda77.android.library.publication")
}

android {
    namespace = "com.edurda77.mooncompass.view"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    api(projects.common)
    implementation(libs.androidx.appcompat)
}