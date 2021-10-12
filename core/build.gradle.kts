plugins {
    id("com.android.library")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(21)
    }

    sourceSets {
        named("main") {
            manifest.srcFile("AndroidManifest.xml")
            res.setSrcDirs(listOf("res"))
        }
    }

    libraryVariants.all {
        generateBuildConfigProvider?.configure {
            enabled = false
        }
    }
}