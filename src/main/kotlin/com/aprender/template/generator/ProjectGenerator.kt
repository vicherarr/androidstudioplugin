package com.aprender.template.generator

import java.io.File

object ProjectGenerator {

    fun generate(
        targetDir: File,
        appName: String,
        packageName: String,
        dbName: String = "app_database.db",
        baseUrl: String = "https://jsonplaceholder.typicode.com/"
    ) {
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        val packagePath = packageName.replace('.', '/')

        // Root files
        writeFile(File(targetDir, "build.gradle.kts"), FileTemplates.getRootBuildGradleKts())
        writeFile(File(targetDir, "settings.gradle.kts"), FileTemplates.getSettingsGradleKts(appName))
        writeFile(File(targetDir, ".gitignore"), FileTemplates.getGitIgnore())
        writeFile(File(targetDir, "README.md"), FileTemplates.getReadme(appName, packageName))

        // Sobreescrituras del scaffold de Android Studio que romperían el build
        writeFile(File(targetDir, "gradle.properties"), FileTemplates.getGradleProperties())
        writeFile(File(targetDir, "gradle/wrapper/gradle-wrapper.properties"), FileTemplates.getGradleWrapperProperties())

        // gradle/libs.versions.toml
        val gradleDir = File(targetDir, "gradle")
        writeFile(File(gradleDir, "libs.versions.toml"), FileTemplates.getLibsVersionsToml())

        // app module root
        val appDir = File(targetDir, "app")
        writeFile(File(appDir, "build.gradle.kts"), FileTemplates.getAppBuildGradleKts(packageName))
        writeFile(File(appDir, "proguard-rules.pro"), "# Add proguard rules here\n")

        // app/src/main
        val mainDir = File(appDir, "src/main")
        writeFile(File(mainDir, "AndroidManifest.xml"), FileTemplates.getAndroidManifestXml(packageName))

        // app/src/main/res
        val resValuesDir = File(mainDir, "res/values")
        writeFile(File(resValuesDir, "strings.xml"), FileTemplates.getStringXml(appName))
        writeFile(File(resValuesDir, "themes.xml"), FileTemplates.getThemesXml())
        writeFile(File(mainDir, "res/values-night/themes.xml"), FileTemplates.getThemesNightXml())

        // app/src/main/java/<packagePath>
        val codeDir = File(mainDir, "java/$packagePath")

        // Root app classes
        writeFile(File(codeDir, "MainApplication.kt"), FileTemplates.getMainApplicationKt(packageName))
        writeFile(File(codeDir, "MainActivity.kt"), FileTemplates.getMainActivityKt(packageName))

        // Data layer
        writeFile(File(codeDir, "data/local/entity/ItemEntity.kt"), FileTemplates.getItemEntityKt(packageName))
        writeFile(File(codeDir, "data/local/dao/ItemDao.kt"), FileTemplates.getItemDaoKt(packageName))
        writeFile(File(codeDir, "data/local/database/AppDatabase.kt"), FileTemplates.getAppDatabaseKt(packageName))

        writeFile(File(codeDir, "data/remote/dto/ItemDto.kt"), FileTemplates.getItemDtoKt(packageName))
        writeFile(File(codeDir, "data/remote/api/ApiService.kt"), FileTemplates.getApiServiceKt(packageName))

        writeFile(File(codeDir, "data/repository/ItemRepositoryImpl.kt"), FileTemplates.getItemRepositoryImplKt(packageName))

        // DI layer
        writeFile(File(codeDir, "di/DatabaseModule.kt"), FileTemplates.getDatabaseModuleKt(packageName, dbName))
        writeFile(File(codeDir, "di/NetworkModule.kt"), FileTemplates.getNetworkModuleKt(packageName, baseUrl))
        writeFile(File(codeDir, "di/RepositoryModule.kt"), FileTemplates.getRepositoryModuleKt(packageName))

        // Domain layer
        writeFile(File(codeDir, "domain/model/Item.kt"), FileTemplates.getItemKt(packageName))
        writeFile(File(codeDir, "domain/repository/ItemRepository.kt"), FileTemplates.getItemRepositoryKt(packageName))
        writeFile(File(codeDir, "domain/usecase/GetItemsUseCase.kt"), FileTemplates.getGetItemsUseCaseKt(packageName))
        writeFile(File(codeDir, "domain/usecase/GetItemUseCase.kt"), FileTemplates.getGetItemUseCaseKt(packageName))

        // UI layer
        writeFile(File(codeDir, "ui/navigation/AppDestinations.kt"), FileTemplates.getAppDestinationsKt(packageName))
        writeFile(File(codeDir, "ui/navigation/AppNavHost.kt"), FileTemplates.getAppNavHostKt(packageName))

        writeFile(File(codeDir, "ui/main/MainScreen.kt"), FileTemplates.getMainScreenKt(packageName, appName))
        writeFile(File(codeDir, "ui/main/MainUiState.kt"), FileTemplates.getMainUiStateKt(packageName))
        writeFile(File(codeDir, "ui/main/MainViewModel.kt"), FileTemplates.getMainViewModelKt(packageName))

        writeFile(File(codeDir, "ui/detail/DetailScreen.kt"), FileTemplates.getDetailScreenKt(packageName))
        writeFile(File(codeDir, "ui/detail/DetailUiState.kt"), FileTemplates.getDetailUiStateKt(packageName))
        writeFile(File(codeDir, "ui/detail/DetailViewModel.kt"), FileTemplates.getDetailViewModelKt(packageName))

        writeFile(File(codeDir, "ui/theme/Color.kt"), FileTemplates.getColorKt(packageName))
        writeFile(File(codeDir, "ui/theme/Theme.kt"), FileTemplates.getThemeKt(packageName))
        writeFile(File(codeDir, "ui/theme/Type.kt"), FileTemplates.getTypeKt(packageName))
    }

    private fun writeFile(file: File, content: String) {
        file.parentFile?.mkdirs()
        file.writeText(content)
    }
}
