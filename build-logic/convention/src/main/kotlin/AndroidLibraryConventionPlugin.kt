import com.android.build.gradle.LibraryExtension
import com.edurda77.convention.ANDROID_MIN_SDK
import com.edurda77.convention.ANDROID_TARGET_SDK
import com.edurda77.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.minSdk = ANDROID_MIN_SDK
                defaultConfig.targetSdk = ANDROID_TARGET_SDK
                defaultConfig.consumerProguardFiles("consumer-rules.pro")
            }
        }
    }
}
