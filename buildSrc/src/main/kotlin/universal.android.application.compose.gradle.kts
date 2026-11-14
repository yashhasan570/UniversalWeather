
import universal.buildlogic.configureCompose
import com.android.build.api.dsl.ApplicationExtension

plugins {
    id("com.android.application")

    id("universal.code.lint")
}

configure<ApplicationExtension> {
    configureCompose()
}
