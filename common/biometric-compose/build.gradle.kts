    plugins {
        id("linky.android.library")
        id("linky.android.compose")
    }

    android {
        namespace = "com.linky.common.biometric_compose"
    }

    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.process)
        implementation(libs.androidx.biometric)

        implementation(libs.androidx.compose.runtime)
        implementation(libs.androidx.ui)
    }