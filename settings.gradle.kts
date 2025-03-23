pluginManagement {
    repositories {
        // Gradle Plugin Portal is the primary source for plugins
        gradlePluginPortal()
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // Google first, for Android-specific dependencies
        google()
        // Then Maven Central for general-purpose libraries
        mavenCentral()
        // Finally, JitPack
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "GreenPulse"
include(":app")