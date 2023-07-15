plugins {
    id("linky.kotlin.library")
}

dependencies {
    implementation(projects.core.dataStore)
    implementation(libs.javax)
    implementation(libs.coroutine.core)
}