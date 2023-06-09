buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
//    dependencies {
//        classpath(libs.firebase.crashlytics.gradle)
//        classpath(libs.android.play.services)
//    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}