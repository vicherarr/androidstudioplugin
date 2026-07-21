import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.7.0"
}

group = "com.aprender.template"
version = "1.0.3"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// La API wizard-template de Android Studio no es estable entre versiones, así que el
// plugin debe compilarse contra la MISMA versión de Android Studio donde se instalará.
// Por defecto usa la instalación local de JetBrains Toolbox; se puede sobreescribir con
// ./gradlew buildPlugin -PandroidStudioPath=/ruta/a/android-studio
val androidStudioPath = providers.gradleProperty("androidStudioPath")
    .orElse(System.getProperty("user.home") + "/.local/share/JetBrains/Toolbox/apps/android-studio")

dependencies {
    intellijPlatform {
        local(androidStudioPath.get())
        bundledPlugin("org.jetbrains.android")
    }
    // Solo para compilar: en runtime la stdlib la aporta el propio IDE
    compileOnly(kotlin("stdlib"))
}

intellijPlatform {
    buildSearchableOptions = false
    instrumentCode = false

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "261"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}
