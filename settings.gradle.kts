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
    }
}
rootProject.name = "linky"
include(
    ":app",
    ":common:intercation",
    ":core:design-system",
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
)