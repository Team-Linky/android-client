import com.android.build.gradle.LibraryExtension
import com.linky.convention.VersionConstants
import com.linky.convention.configureBuildConfig
import com.linky.convention.configureKotlinAndroid
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
                configureBuildConfig()
                defaultConfig.targetSdk = VersionConstants.TARGET_SDK
            }
        }
    }
}