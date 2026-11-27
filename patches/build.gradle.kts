import com.android.tools.r8.CompilationMode
import com.android.tools.r8.D8
import com.android.tools.r8.D8Command
import com.android.tools.r8.OutputMode
import com.android.tools.r8.utils.ArchiveResourceProvider
import ru.vyarus.gradle.plugin.animalsniffer.AnimalSniffer

plugins {
    // Fails the build if the patches file uses JDK APIs that do not exist on Android.
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
    // Checking only the compiled sources would miss bundled dependencies,
    // so the whole patches file is checked by checkAndroidApi below.
    sourceSets = emptyList()
}

tasks {
    val patchesFile = jar.flatMap { it.archiveFile }

    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
        argumentProviders.add(CommandLineArgumentProvider {
            listOf(patchesFile.get().asFile.absolutePath)
        })
    }

    // Runs D8 on every class in the patches file (the patches and all bundled dependencies)
    // but outputs class files instead of dex, so the check sees the code as it runs on Android,
    // after D8 has replaced methods it backports such as List.of() and Optional.isEmpty().
    val desugarPatchesClasses = register("desugarPatchesClasses") {
        val classpath = sourceSets["main"].compileClasspath
        val output = layout.buildDirectory.file("animalsniffer/patches-desugared.jar")

        inputs.file(patchesFile)
        inputs.files(classpath)
        outputs.file(output)

        doLast {
            val patchesPath = patchesFile.get().asFile.toPath()
            val outputPath = output.get().asFile.toPath()

            D8.run(
                D8Command.builder()
                    .addProgramResourceProvider(ArchiveResourceProvider.fromArchive(patchesPath, true))
                    .addClasspathFiles(classpath.files.filter { it.exists() }.map { it.toPath() })
                    .setMode(CompilationMode.DEBUG)
                    .setMinApiLevel(26)
                    .setOutput(outputPath, OutputMode.ClassFile)
                    .build()
            )
        }
    }

    val extractPatchesClasses = register<Sync>("extractPatchesClasses") {
        from(desugarPatchesClasses.map { zipTree(it.outputs.files.singleFile) }) {
            include("**/*.class")
        }
        into(layout.buildDirectory.dir("animalsniffer/patches-classes"))
    }

    val checkAndroidApi = register<AnimalSniffer>("checkAndroidApi") {
        description = "Checks that the patches file only uses APIs available on Android."
        group = "verification"

        source(extractPatchesClasses)
        classpath = sourceSets["main"].compileClasspath
        animalsnifferSignatures = configurations["signature"]
        animalsnifferClasspath = configurations["animalsniffer"]
        // Only used to show source lines in the report.
        sourcesDirs = sourceSets["main"].allSource.sourceDirectories
        reports.text.outputLocation = layout.buildDirectory.file("reports/animalsniffer/patches.text")
        reports.csv.outputLocation = layout.buildDirectory.file("reports/animalsniffer/patches.csv")
    }

    check {
        dependsOn(checkAndroidApi)
    }

    // buildAndroid does not depend on check.
    named("buildAndroid") {
        dependsOn(checkAndroidApi)
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}
