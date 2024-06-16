plugins {
    id("linky.android.library")
    id("linky.android.hilt")
}

android {
    namespace = "com.linky.data"
}

dependencies {
    implementation(projects.core.dataStore)
    implementation(projects.core.dataBase)
    implementation(projects.core.model)

    implementation(libs.coroutine.core)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.room.paging)
}