plugins {
    id("linky.android.library")
}

android {
    namespace = "com.linky.common.activity_stack_counter"
}

dependencies {
    implementation(libs.javax)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.process)
}