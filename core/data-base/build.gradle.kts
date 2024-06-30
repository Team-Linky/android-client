plugins {
    id("linky.android.library")
    id("linky.android.hilt")
    id("androidx.room")
}

android {
    namespace = "com.linky.data_base"

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.androidx.room)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
}