extension {
    name = "extensions/extension.mpe"
}

android {
    namespace = "app.morphe.extension"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        targetSdk = 35
        //multiDexEnabled = true
        versionCode = 1
    }

    compileOptions {
        //isCoreLibraryDesugaringEnabled = true
        //sourceCompatibility = JavaVersion.VERSION_1_8
        //targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.jodatime)
    implementation(libs.jodaconvert)
}
