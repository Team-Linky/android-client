plugins {
    id("linky.android.library")
    id("linky.android.compose")
    id("linky.android.hilt")
}

android {
    namespace = "com.linky.core.testing"
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.data)

    debugApi(libs.androidx.compose.ui.test.manifest)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.datetime)
}