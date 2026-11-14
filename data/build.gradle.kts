import com.android.build.api.dsl.LibraryExtension

plugins {
    id("universal.library")
    kotlin("plugin.serialization")
    id("app.cash.sqldelight")
}

configure<LibraryExtension> {
    namespace = "com.universalweather.app.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    sqldelight {
        databases {
            create("Database") {
                packageName.set("com.universalweather.app.data")
                dialect(libs.sqldelight.dialects.sql)
                schemaOutputDirectory.set(project.file("./src/main/sqldelight"))
            }
        }
    }
}

dependencies {
    implementation(projects.domain)
    implementation(projects.weatherUnit)

    api(libs.bundles.sqldelight)
}
