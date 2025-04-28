import com.android.build.api.dsl.ApplicationExtension
import com.edurda77.convention.ANDROID_TARGET_SDK
import com.edurda77.convention.configureAndroidCompose
import com.edurda77.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            val extension = extensions.getByType<ApplicationExtension>()
            extension.defaultConfig.targetSdk = ANDROID_TARGET_SDK
            configureKotlinAndroid(extension)
            configureAndroidCompose(extension)
        }
    }
}