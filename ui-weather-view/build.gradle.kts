import com.android.build.api.dsl.LibraryExtension

plugins {
    id("universal.library")
}

configure<LibraryExtension> {
    namespace = "com.universalweather.app.ui.theme.weatherView"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.core.ktx)
}
