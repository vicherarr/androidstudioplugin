<recipe>
    <instantiate src="root/build.gradle.kts.ftl" to="build.gradle.kts" />
    <instantiate src="root/settings.gradle.kts.ftl" to="settings.gradle.kts" />
    <instantiate src="root/gitignore.ftl" to=".gitignore" />
    <instantiate src="root/README.md.ftl" to="README.md" />
    <instantiate src="root/gradle/libs.versions.toml.ftl" to="gradle/libs.versions.toml" />
    <instantiate src="root/app/build.gradle.kts.ftl" to="app/build.gradle.kts" />
    <instantiate src="root/app/proguard-rules.pro.ftl" to="app/proguard-rules.pro" />
    <instantiate src="root/app/src/main/AndroidManifest.xml.ftl" to="${manifestOut}/AndroidManifest.xml" />
    <instantiate src="root/app/src/main/res/values/strings.xml.ftl" to="${resOut}/values/strings.xml" />
    <instantiate src="root/app/src/main/res/values/themes.xml.ftl" to="${resOut}/values/themes.xml" />

    <instantiate src="root/app/src/main/java/MainApplication.kt.ftl" to="${srcOut}/MainApplication.kt" />
    <instantiate src="root/app/src/main/java/MainActivity.kt.ftl" to="${srcOut}/MainActivity.kt" />
    <instantiate src="root/app/src/main/java/data/local/entity/ItemEntity.kt.ftl" to="${srcOut}/data/local/entity/ItemEntity.kt" />
    <instantiate src="root/app/src/main/java/data/local/dao/ItemDao.kt.ftl" to="${srcOut}/data/local/dao/ItemDao.kt" />
    <instantiate src="root/app/src/main/java/data/local/database/AppDatabase.kt.ftl" to="${srcOut}/data/local/database/AppDatabase.kt" />
    <instantiate src="root/app/src/main/java/data/remote/dto/ItemDto.kt.ftl" to="${srcOut}/data/remote/dto/ItemDto.kt" />
    <instantiate src="root/app/src/main/java/data/remote/api/ApiService.kt.ftl" to="${srcOut}/data/remote/api/ApiService.kt" />
    <instantiate src="root/app/src/main/java/data/repository/ItemRepository.kt.ftl" to="${srcOut}/data/repository/ItemRepository.kt" />
    <instantiate src="root/app/src/main/java/data/repository/ItemRepositoryImpl.kt.ftl" to="${srcOut}/data/repository/ItemRepositoryImpl.kt" />
    <instantiate src="root/app/src/main/java/di/DatabaseModule.kt.ftl" to="${srcOut}/di/DatabaseModule.kt" />
    <instantiate src="root/app/src/main/java/di/NetworkModule.kt.ftl" to="${srcOut}/di/NetworkModule.kt" />
    <instantiate src="root/app/src/main/java/domain/model/Item.kt.ftl" to="${srcOut}/domain/model/Item.kt" />
    <instantiate src="root/app/src/main/java/domain/usecase/GetItemsUseCase.kt.ftl" to="${srcOut}/domain/usecase/GetItemsUseCase.kt" />
    <instantiate src="root/app/src/main/java/ui/main/MainScreen.kt.ftl" to="${srcOut}/ui/main/MainScreen.kt" />
    <instantiate src="root/app/src/main/java/ui/main/MainUiState.kt.ftl" to="${srcOut}/ui/main/MainUiState.kt" />
    <instantiate src="root/app/src/main/java/ui/main/MainViewModel.kt.ftl" to="${srcOut}/ui/main/MainViewModel.kt" />
    <instantiate src="root/app/src/main/java/ui/theme/Color.kt.ftl" to="${srcOut}/ui/theme/Color.kt" />
    <instantiate src="root/app/src/main/java/ui/theme/Theme.kt.ftl" to="${srcOut}/ui/theme/Theme.kt" />
    <instantiate src="root/app/src/main/java/ui/theme/Type.kt.ftl" to="${srcOut}/ui/theme/Type.kt" />
</recipe>
