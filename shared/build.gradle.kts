plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("io.github.skeptick.libres")
    id("app.cash.sqldelight")
}

version = "1.0-SNAPSHOT"

kotlin {

    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Shared Code"
        homepage = "https://github.com/AlexGladkov/JetpackComposeDemo"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {

        commonMain {
            dependencies {

                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)

                implementation(libs.odyssey.core)
                implementation(libs.odyssey.compose)

                implementation(libs.klock.common)

                implementation(libs.kviewmodel.core)
                implementation(libs.kviewmodel.compose)
                implementation(libs.kviewmodel.odyssey)

                implementation(libs.kodein)
                implementation(libs.libres.compose)
                implementation(libs.kotlin.serialization)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.sqldelight.native)
           }
        }

        androidMain {
            dependencies {
                implementation(libs.android.material)
                implementation(libs.sqldelight.android)
            }
        }

    }

}

sqldelight {
    databases {
        create("Database") {
            packageName.set("")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/data/schema"))
            migrationOutputDirectory.set(file("src/commonMain/sqldelight/migrations"))
        }
    }
}

libres {
    generatedClassName = "AppRes"
    generateNamedArguments = true
    baseLocaleLanguageCode = "en"
}

android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    namespace = "tech.mobiledeveloper.shared"

    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of("11"))
        }
    }
}