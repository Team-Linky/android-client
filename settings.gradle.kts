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
    ":core:design-system",
    ":core:data-store",
    ":core:di",
    ":core:data",
    ":core:data-base",
    ":core:model",
    ":feature:navigation",
    ":feature:timeline",
    ":feature:tag",
    ":feature:more",
    ":feature:link",
    ":feature:link-url-input",
    ":feature:link-detail-input",
    ":feature:link-input-complete",
    ":feature:tip",
    ":feature:more-activity",
    ":feature:lock",
    ":feature:certification",
    ":feature:certification-registration",
    ":feature:webview",
)
