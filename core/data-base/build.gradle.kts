plugins {
    id("linky.android.library")
    id("linky.android.hilt")
}

android {
    namespace = "com.linky.data_base"
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.androidx.room)
    implementation(libs.moshi)
    kapt(libs.moshi.codegen)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)
    kapt(libs.androidx.room.compiler)
}