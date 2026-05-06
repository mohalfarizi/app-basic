plugins {
    alias(libs.plugins.android.library)
//    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "com.eji14.cattycat"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
//    compilerOptions {
//        jvmTarget = JvmTarget.JVM_11
//    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
//    debugImplementation(libs.androidx.compose.ui.tooling)

    // Activity Compose for BackHandler
    implementation(libs.androidx.activity.compose)
    
    // ConstraintLayout Compose
    implementation(libs.androidx.constraintlayout.compose)
}
group = "com.github.mohalfarizi"
version = "v1.0.0"
afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.mohalfarizi"
                artifactId = "cattycat"
                version = "v1.0.0"
            }
        }
    }

// Wire up the component AFTER android block has registered it
    components.whenObjectAdded {
        if (this.name == "release") {
            publishing.publications.withType<MavenPublication>().named("release") {
                from(this@whenObjectAdded)
            }
        }
    }
}
