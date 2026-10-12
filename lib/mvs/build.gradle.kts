plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.vanniktech.maven.publish)
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

android {
    namespace = "xyz.icode.mvs"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(group.toString(), "mvs", version.toString())

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
