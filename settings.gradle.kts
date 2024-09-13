pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}
rootProject.name = "linky"
include(
    ":app",
    ":common:intercation",
    ":common:process-lifecycle",
    ":common:safe-coroutine",
    ":common:biometric-compose",
    ":core:design-system",
    ":core:data-store",
    ":core:di",
    ":core:data",
    ":core:data-test",
    ":core:data-base",
    ":core:model",
    ":core:testing",
    ":feature:navigation",
    ":feature:timeline",
    ":feature:tag",
    ":feature:tag-setting",
    ":feature:more",
    ":feature:link",
    ":feature:link-url-input",
    ":feature:link-modifier",
    ":feature:link-input-complete",
    ":feature:recycle-bin",
    ":feature:tip",
    ":feature:more-activity",
    ":feature:lock",
    ":feature:pin",
    ":feature:pin-setting",
    ":feature:webview",
    ":feature:ask",
    ":feature:backup-restore",
)