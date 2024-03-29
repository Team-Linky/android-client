plugins {
    id("linky.android.library")
    id("linky.android.hilt")
}

android {
    namespace = "com.linky.di"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.dataBase)
    implementation(projects.core.dataStore)
    implementation(projects.common.processLifecycle)

    implementation(libs.androidx.room)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation(libs.okhttp)
    implementation(libs.data.store)
    implementation(libs.retrofit.logging)
    implementation(libs.moshi)
    kapt(libs.moshi.codegen)
}