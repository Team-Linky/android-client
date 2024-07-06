plugins {
    id("linky.android.library")
    id("linky.android.compose")
    id("linky.android.hilt")
}

android {
    namespace = "com.linky.core.testing"
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.data)
    implementation(projects.core.dataBase)
    implementation(projects.core.di)
    implementation(projects.core.model)

    debugApi(libs.androidx.compose.ui.test.manifest)

    implementation(libs.moshi)
    implementation(libs.androidx.paging.common.jvm)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.room.testing)
    implementation(libs.hilt.android.testing)

    implementation(libs.coroutine.test)
    implementation(libs.kotlinx.datetime)
}