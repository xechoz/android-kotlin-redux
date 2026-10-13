import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.vanniktech.maven.publish)
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "xyz.icode.mvs"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    pom {
        name = "MvState"
        description = "Not RxJava — a minimal Redux built on Kotlin Coroutines Flow."
        inceptionYear = "2025"
        url = "https://github.com/xechoz/android-kotlin-redux"
        licenses {
            license {
                name = "GNU General Public License v3.0"
                url = "https://www.gnu.org/licenses/gpl-3.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "xechoz"
                name = "xechoz"
                url = "https://github.com/xechoz"
            }
        }
        scm {
            url = "https://github.com/xechoz/android-kotlin-redux"
            connection = "scm:git:https://github.com/xechoz/android-kotlin-redux.git"
            developerConnection = "scm:git:ssh://git@github.com/xechoz/android-kotlin-redux.git"
        }
    }
}
