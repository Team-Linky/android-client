plugins {
    id("linky.android.library")
    id("linky.android.hilt")
}

android {
    namespace = "com.linky.di"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.dataStore)
    implementation(projects.common.processLifecycle)

    implementation(libs.okhttp)
    implementation(libs.data.store)
    implementation(libs.retrofit.logging)
}