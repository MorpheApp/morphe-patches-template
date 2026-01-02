group = "app.morphe"

patches {
    about {
        name = "Morphe Patches template"
        description = "Patches template for Morphe"
        source = "git@github.com:MorpheApp/morphe-patches-template.git"
        author = "Morphe"
        contact = "na"
        website = "https://morphe.software"
        license = "Additional conditions under GPL section 7 apply: attribution and project name restrictions. See LICENSE file."
    }
}

dependencies {
    // Used by JsonGenerator.
    implementation(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("app.morphe.util.PatchListGeneratorKt")
    }
    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/MorpheApp/morphe-patches-template")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
