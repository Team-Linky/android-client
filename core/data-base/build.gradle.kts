plugins {
    id("linky.android.library")
    id("linky.android.hilt")
    id("androidx.room")
}

android {
    namespace = "com.linky.core.data_base"

    room {
        schemaDirectory("$projectDir/schemas")
    }

    packagingOptions.resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
        merges += "META-INF/LICENSE.md"
        merges += "META-INF/LICENSE-notice.md"
    }

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.androidx.room)
    implementation(libs.moshi)
    implementation(libs.androidx.paging.testing.android)
    ksp(libs.moshi.codegen)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.junit.engine)

    androidTestImplementation(libs.mannodermaus.test.core)
    androidTestRuntimeOnly(libs.mannodermaus.test.runner)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.paging.common)

    androidTestImplementation(libs.coroutine.test)
    androidTestImplementation(libs.mockk.jvm)
    androidTestImplementation(libs.mockk.agent)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.orbit.test)
}