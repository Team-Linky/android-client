plugins {
    id("linky.android.library")
    id("linky.android.compose")
    id("linky.android.hilt")
}

android {
    namespace = "com.linky.feature.link_modifier"
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.feature.navigation)
    implementation(projects.feature.linkUrlInput)

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
    implementation(libs.system.ui.controller)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.hilt.navigation)
    implementation(libs.androidx.paging.compose)
    implementation(libs.navigation.animation)
    implementation(libs.opengraph)
    implementation(libs.orbit.compose)
    implementation(libs.orbit.viewmodel)
}