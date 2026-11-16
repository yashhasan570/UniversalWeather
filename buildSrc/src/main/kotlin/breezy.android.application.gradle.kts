
import universal.buildlogic.AndroidConfig
import universal.buildlogic.configureAndroidApplication
import universal.buildlogic.configureTest
import com.android.build.api.dsl.ApplicationExtension

plugins {
    id("com.android.application")

    id("universal.code.lint")
}

configure<ApplicationExtension> {
    defaultConfig {
        targetSdk = AndroidConfig.TARGET_SDK
    }
    configureAndroidApplication()
    configureTest()
}
