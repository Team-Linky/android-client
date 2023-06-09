plugins {
    id("linky.android.library")
    id("linky.android.library.compose")
    id("linky.android.hilt")
}

android {
    namespace = "com.linky.design_system"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.lifecycle.runtime)
    implementation(libs.androidx.compose.lifecycle.viewmodel)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material.compose)
    implementation(libs.coil)
    implementation(libs.lottie)
    implementation(libs.system.ui.controller)
    implementation(libs.androidx.compose.navigation)
}