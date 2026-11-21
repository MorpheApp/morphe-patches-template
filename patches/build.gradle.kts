plugins {
    // Fails the build if patch code uses JDK APIs that do not exist on Android.
    alias(libs.plugins.animalsniffer)
}

group = "app.template"

patches {
    // TODO: Update this section with your project details.
    about {
        name = "UserXYZ Patches"
        description = "Patches for apps I like"
        source = "git@github.com:UserXYZ/morphe-patches.git"
        author = "Awesome dev"
        contact = "na"
        website = "na"
        license = "GPLv3"
    }
}

// Separate configuration so gson is available at runtime for the
// generatePatchesList task but never bundled into the APK.
val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)

    // Android API level 26 is the minimum API the patches are compiled for (see buildAndroid).
    signature(libs.android.api.signature) { artifact { type = "signature" } }
}

animalsniffer {
    // Only the main source set ships in the patches file.
    sourceSets = listOf(project.sourceSets["main"])
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    // The check task already runs it, but buildAndroid does not depend on check.
    named("buildAndroid") {
        dependsOn("animalsnifferMain")
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}
