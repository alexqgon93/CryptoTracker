pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Crypto Tracker"
include(":app")
include(":feature:coins")
include(":core:ui")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:testing")
