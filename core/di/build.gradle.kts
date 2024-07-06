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
    ksp(libs.androidx.room.compiler)

    implementation(libs.moshi)
    implementation(libs.data.store)
    ksp(libs.moshi.codegen)
}