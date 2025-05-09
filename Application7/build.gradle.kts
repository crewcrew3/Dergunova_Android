// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization.plugin) apply false
    alias(libs.plugins.gradle.secrets.plugin) apply false
    alias(libs.plugins.hilt.plugin) apply false
}