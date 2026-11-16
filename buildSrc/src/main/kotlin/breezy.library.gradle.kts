import universal.buildlogic.configureAndroidLibrary
import universal.buildlogic.configureTest
import com.android.build.api.dsl.LibraryExtension

plugins {
    id("com.android.library")

    id("universal.code.lint")
}

configure<LibraryExtension> {
    configureAndroidLibrary()
    configureTest()
}
