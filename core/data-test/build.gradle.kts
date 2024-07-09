plugins {
    id("linky.android.library")
    id("linky.android.hilt")
    id("de.mannodermaus.android-junit5")
}

android {
    namespace = "com.linky.core.data_test"

    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    implementation(projects.core.dataBase)
    implementation(libs.moshi)

    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.feature.timeline)


    implementation(libs.junit)
    implementation(libs.junit.engine)

    androidTestImplementation(libs.mannodermaus.test.core)
    androidTestRuntimeOnly(libs.mannodermaus.test.runner)

    implementation(libs.androidx.junit)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.core.testing)
    implementation(libs.hilt.android.testing)
    implementation(libs.androidx.room.testing)
    implementation(libs.androidx.paging.common)

    implementation(libs.coroutine.test)
    implementation(libs.mockk.jvm)
    implementation(libs.mockk.agent)
    implementation(libs.mockk.android)
    implementation(libs.orbit.test)
}