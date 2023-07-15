plugins {
    id("linky.android.library")
}

android {
    namespace = "com.linky.process_lifecycle"
}

dependencies {
    implementation(libs.javax)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.process)
}