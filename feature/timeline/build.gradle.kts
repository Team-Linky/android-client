plugins {
    id("linky.android.library")
    id("linky.android.compose")
    id("linky.android.hilt")
}

android {
    namespace = "com.linky.timeline"
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.dataBase)
    implementation(projects.feature.navigation)
    implementation(projects.feature.webview)

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
    implementation(libs.orbit.compose)
    implementation(libs.orbit.viewmodel)
    implementation(libs.balloon)
    implementation(libs.system.ui.controller)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.hilt.navigation)
    implementation(libs.navigation.animation)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.compose)
}
