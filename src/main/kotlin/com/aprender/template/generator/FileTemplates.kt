package com.aprender.template.generator

object FileTemplates {

    fun getRootBuildGradleKts(): String = """
        plugins {
            alias(libs.plugins.android.application) apply false
            alias(libs.plugins.kotlin.android) apply false
            alias(libs.plugins.kotlin.compose) apply false
            alias(libs.plugins.kotlin.serialization) apply false
            alias(libs.plugins.hilt.android) apply false
            alias(libs.plugins.ksp) apply false
            alias(libs.plugins.room) apply false
        }
    """.trimIndent()

    fun getSettingsGradleKts(appName: String): String = """
        pluginManagement {
            repositories {
                google {
                    content {
                        includeGroupByRegex("com\\.android.*")
                        includeGroupByRegex("com\\.google.*")
                        includeGroupByRegex("androidx.*")
                    }
                }
                mavenCentral()
                gradlePluginPortal()
            }
        }
        dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                google()
                mavenCentral()
            }
        }

        rootProject.name = "$appName"
        include(":app")
    """.trimIndent()

    fun getLibsVersionsToml(): String = """
        [versions]
        agp = "8.11.1"
        kotlin = "2.1.21"
        ksp = "2.1.21-2.0.2"
        hilt = "2.58"
        retrofit = "3.0.0"
        kotlinxSerialization = "1.8.1"
        okhttp = "4.12.0"
        room = "2.8.4"
        coreKtx = "1.16.0"
        lifecycleRuntimeKtx = "2.9.1"
        activityCompose = "1.10.1"
        composeBom = "2025.06.00"
        hiltNavigationCompose = "1.2.0"

        [libraries]
        androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
        androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
        androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
        androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
        androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
        androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
        androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
        androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
        androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
        androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }

        hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
        hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
        hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }

        retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
        retrofit-converter-kotlinx-serialization = { group = "com.squareup.retrofit2", name = "converter-kotlinx-serialization", version.ref = "retrofit" }
        okhttp-logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
        kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }

        room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
        room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

        [plugins]
        android-application = { id = "com.android.application", version.ref = "agp" }
        kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
        kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
        kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
        hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
        ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
        room = { id = "androidx.room", version.ref = "room" }
    """.trimIndent()

    fun getAppBuildGradleKts(packageName: String): String = """
        plugins {
            alias(libs.plugins.android.application)
            alias(libs.plugins.kotlin.android)
            alias(libs.plugins.kotlin.compose)
            alias(libs.plugins.kotlin.serialization)
            alias(libs.plugins.ksp)
            alias(libs.plugins.hilt.android)
            alias(libs.plugins.room)
        }

        android {
            namespace = "$packageName"
            compileSdk = 36

            defaultConfig {
                applicationId = "$packageName"
                minSdk = 26
                targetSdk = 36
                versionCode = 1
                versionName = "1.0"

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            kotlinOptions {
                jvmTarget = "17"
            }
            buildFeatures {
                compose = true
                buildConfig = true
            }
        }

        room {
            schemaDirectory("${'$'}projectDir/schemas")
        }

        dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.activity.compose)
            implementation(platform(libs.androidx.compose.bom))
            implementation(libs.androidx.compose.ui)
            implementation(libs.androidx.compose.ui.graphics)
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.compose.material3)
            debugImplementation(libs.androidx.compose.ui.tooling)

            // Hilt
            implementation(libs.hilt.android)
            ksp(libs.hilt.compiler)
            implementation(libs.hilt.navigation.compose)

            // Retrofit & Serialization
            implementation(libs.retrofit)
            implementation(libs.retrofit.converter.kotlinx.serialization)
            implementation(libs.okhttp.logging.interceptor)
            implementation(libs.kotlinx.serialization.json)

            // Room
            implementation(libs.room.runtime)
            ksp(libs.room.compiler)
        }
    """.trimIndent()

    fun getAndroidManifestXml(packageName: String): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android">

            <uses-permission android:name="android.permission.INTERNET" />

            <application
                android:name=".MainApplication"
                android:allowBackup="true"
                android:icon="@mipmap/ic_launcher"
                android:label="@string/app_name"
                android:roundIcon="@mipmap/ic_launcher_round"
                android:supportsRtl="true"
                android:theme="@style/Theme.App">
                <activity
                    android:name=".MainActivity"
                    android:exported="true"
                    android:theme="@style/Theme.App">
                    <intent-filter>
                        <action android:name="android.intent.action.MAIN" />
                        <category android:name="android.intent.category.LAUNCHER" />
                    </intent-filter>
                </activity>
            </application>

        </manifest>
    """.trimIndent()

    fun getMainApplicationKt(packageName: String): String = """
        package $packageName

        import android.app.Application
        import dagger.hilt.android.HiltAndroidApp

        @HiltAndroidApp
        class MainApplication : Application()
    """.trimIndent()

    fun getMainActivityKt(packageName: String): String = """
        package $packageName

        import android.os.Bundle
        import androidx.activity.ComponentActivity
        import androidx.activity.compose.setContent
        import androidx.activity.enableEdgeToEdge
        import androidx.compose.foundation.layout.fillMaxSize
        import androidx.compose.material3.MaterialTheme
        import androidx.compose.material3.Surface
        import androidx.compose.ui.Modifier
        import $packageName.ui.main.MainScreen
        import $packageName.ui.theme.AppTheme
        import dagger.hilt.android.AndroidEntryPoint

        @AndroidEntryPoint
        class MainActivity : ComponentActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                enableEdgeToEdge()
                setContent {
                    AppTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainScreen()
                        }
                    }
                }
            }
        }
    """.trimIndent()

    fun getItemEntityKt(packageName: String): String = """
        package $packageName.data.local.entity

        import androidx.room.Entity
        import androidx.room.PrimaryKey

        @Entity(tableName = "items")
        data class ItemEntity(
            @PrimaryKey val id: Int,
            val title: String,
            val description: String,
            val createdAt: Long = System.currentTimeMillis()
        )
    """.trimIndent()

    fun getItemDaoKt(packageName: String): String = """
        package $packageName.data.local.dao

        import androidx.room.Dao
        import androidx.room.Insert
        import androidx.room.OnConflictStrategy
        import androidx.room.Query
        import $packageName.data.local.entity.ItemEntity
        import kotlinx.coroutines.flow.Flow

        @Dao
        interface ItemDao {
            @Query("SELECT * FROM items ORDER BY createdAt DESC")
            fun getItems(): Flow<List<ItemEntity>>

            @Insert(onConflict = OnConflictStrategy.REPLACE)
            suspend fun insertItems(items: List<ItemEntity>)

            @Query("DELETE FROM items")
            suspend fun clearAll()
        }
    """.trimIndent()

    fun getAppDatabaseKt(packageName: String): String = """
        package $packageName.data.local.database

        import androidx.room.Database
        import androidx.room.RoomDatabase
        import $packageName.data.local.dao.ItemDao
        import $packageName.data.local.entity.ItemEntity

        @Database(entities = [ItemEntity::class], version = 1, exportSchema = true)
        abstract class AppDatabase : RoomDatabase() {
            abstract fun itemDao(): ItemDao
        }
    """.trimIndent()

    fun getItemDtoKt(packageName: String): String = """
        package $packageName.data.remote.dto

        import kotlinx.serialization.SerialName
        import kotlinx.serialization.Serializable

        @Serializable
        data class ItemDto(
            @SerialName("id") val id: Int,
            @SerialName("title") val title: String,
            @SerialName("body") val description: String
        )
    """.trimIndent()

    fun getApiServiceKt(packageName: String): String = """
        package $packageName.data.remote.api

        import $packageName.data.remote.dto.ItemDto
        import retrofit2.http.GET

        interface ApiService {
            @GET("posts")
            suspend fun getItems(): List<ItemDto>
        }
    """.trimIndent()

    fun getItemKt(packageName: String): String = """
        package $packageName.domain.model

        data class Item(
            val id: Int,
            val title: String,
            val description: String
        )
    """.trimIndent()

    fun getItemRepositoryKt(packageName: String): String = """
        package $packageName.domain.repository

        import $packageName.domain.model.Item
        import kotlinx.coroutines.flow.Flow

        interface ItemRepository {
            fun getItems(): Flow<List<Item>>
            suspend fun refreshItems(): Result<Unit>
        }
    """.trimIndent()

    fun getItemRepositoryImplKt(packageName: String): String = """
        package $packageName.data.repository

        import $packageName.data.local.dao.ItemDao
        import $packageName.data.local.entity.ItemEntity
        import $packageName.data.remote.api.ApiService
        import $packageName.domain.repository.ItemRepository
        import $packageName.domain.model.Item
        import kotlinx.coroutines.flow.Flow
        import kotlinx.coroutines.flow.map
        import javax.inject.Inject

        class ItemRepositoryImpl @Inject constructor(
            private val apiService: ApiService,
            private val itemDao: ItemDao
        ) : ItemRepository {

            override fun getItems(): Flow<List<Item>> {
                return itemDao.getItems().map { entities ->
                    entities.map { Item(id = it.id, title = it.title, description = it.description) }
                }
            }

            override suspend fun refreshItems(): Result<Unit> {
                return try {
                    val remoteItems = apiService.getItems()
                    val entities = remoteItems.map { dto ->
                        ItemEntity(id = dto.id, title = dto.title, description = dto.description)
                    }
                    itemDao.insertItems(entities)
                    Result.success(Unit)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }
    """.trimIndent()

    fun getDatabaseModuleKt(packageName: String, dbName: String = "app_database.db"): String = """
        package $packageName.di

        import android.content.Context
        import androidx.room.Room
        import $packageName.data.local.dao.ItemDao
        import $packageName.data.local.database.AppDatabase
        import dagger.Module
        import dagger.Provides
        import dagger.hilt.InstallIn
        import dagger.hilt.android.qualifiers.ApplicationContext
        import dagger.hilt.components.SingletonComponent
        import javax.inject.Singleton

        @Module
        @InstallIn(SingletonComponent::class)
        object DatabaseModule {

            @Provides
            @Singleton
            fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
                return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "$dbName"
                ).build()
            }

            @Provides
            fun provideItemDao(database: AppDatabase): ItemDao {
                return database.itemDao()
            }
        }
    """.trimIndent()

    fun getNetworkModuleKt(packageName: String, baseUrl: String = "https://jsonplaceholder.typicode.com/"): String = """
        package $packageName.di

        import $packageName.BuildConfig
        import $packageName.data.remote.api.ApiService
        import dagger.Module
        import dagger.Provides
        import dagger.hilt.InstallIn
        import dagger.hilt.components.SingletonComponent
        import kotlinx.serialization.json.Json
        import okhttp3.MediaType.Companion.toMediaType
        import okhttp3.OkHttpClient
        import okhttp3.logging.HttpLoggingInterceptor
        import retrofit2.Retrofit
        import retrofit2.converter.kotlinx.serialization.asConverterFactory
        import java.util.concurrent.TimeUnit
        import javax.inject.Singleton

        @Module
        @InstallIn(SingletonComponent::class)
        object NetworkModule {

            private const val BASE_URL = "$baseUrl"

            @Provides
            @Singleton
            fun provideJson(): Json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }

            @Provides
            @Singleton
            fun provideOkHttpClient(): OkHttpClient {
                return OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()
            }

            @Provides
            @Singleton
            fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
                val contentType = "application/json".toMediaType()
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(json.asConverterFactory(contentType))
                    .build()
            }

            @Provides
            @Singleton
            fun provideApiService(retrofit: Retrofit): ApiService {
                return retrofit.create(ApiService::class.java)
            }
        }
    """.trimIndent()

    fun getRepositoryModuleKt(packageName: String): String = """
        package $packageName.di

        import $packageName.data.repository.ItemRepositoryImpl
        import $packageName.domain.repository.ItemRepository
        import dagger.Binds
        import dagger.Module
        import dagger.hilt.InstallIn
        import dagger.hilt.components.SingletonComponent
        import javax.inject.Singleton

        @Module
        @InstallIn(SingletonComponent::class)
        abstract class RepositoryModule {

            @Binds
            @Singleton
            abstract fun bindItemRepository(
                impl: ItemRepositoryImpl
            ): ItemRepository
        }
    """.trimIndent()

    fun getGetItemsUseCaseKt(packageName: String): String = """
        package $packageName.domain.usecase

        import $packageName.domain.repository.ItemRepository
        import $packageName.domain.model.Item
        import kotlinx.coroutines.flow.Flow
        import javax.inject.Inject

        class GetItemsUseCase @Inject constructor(
            private val repository: ItemRepository
        ) {
            operator fun invoke(): Flow<List<Item>> {
                return repository.getItems()
            }

            suspend fun refresh(): Result<Unit> {
                return repository.refreshItems()
            }
        }
    """.trimIndent()

    fun getMainUiStateKt(packageName: String): String = """
        package $packageName.ui.main

        import $packageName.domain.model.Item

        sealed interface MainUiState {
            data object Loading : MainUiState
            data class Success(val items: List<Item>) : MainUiState
            data class Error(val message: String) : MainUiState
        }
    """.trimIndent()

    fun getMainViewModelKt(packageName: String): String = """
        package $packageName.ui.main

        import androidx.lifecycle.ViewModel
        import androidx.lifecycle.viewModelScope
        import $packageName.domain.usecase.GetItemsUseCase
        import dagger.hilt.android.lifecycle.HiltViewModel
        import kotlinx.coroutines.flow.MutableStateFlow
        import kotlinx.coroutines.flow.StateFlow
        import kotlinx.coroutines.flow.asStateFlow
        import kotlinx.coroutines.flow.catch
        import kotlinx.coroutines.launch
        import javax.inject.Inject

        @HiltViewModel
        class MainViewModel @Inject constructor(
            private val getItemsUseCase: GetItemsUseCase
        ) : ViewModel() {

            private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
            val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

            init {
                observeItems()
                refreshData()
            }

            private fun observeItems() {
                viewModelScope.launch {
                    getItemsUseCase()
                        .catch { _uiState.value = MainUiState.Error(it.message ?: "Unknown error") }
                        .collect { items ->
                            _uiState.value = MainUiState.Success(items)
                        }
                }
            }

            fun refreshData() {
                viewModelScope.launch {
                    val result = getItemsUseCase.refresh()
                    if (result.isFailure) {
                        if (_uiState.value is MainUiState.Loading) {
                            _uiState.value = MainUiState.Error(
                                result.exceptionOrNull()?.message ?: "Failed to load data"
                            )
                        }
                    }
                }
            }
        }
    """.trimIndent()

    fun getMainScreenKt(packageName: String, appName: String): String = """
        package $packageName.ui.main

        import androidx.compose.foundation.layout.Box
        import androidx.compose.foundation.layout.Column
        import androidx.compose.foundation.layout.Spacer
        import androidx.compose.foundation.layout.fillMaxSize
        import androidx.compose.foundation.layout.fillMaxWidth
        import androidx.compose.foundation.layout.height
        import androidx.compose.foundation.layout.padding
        import androidx.compose.foundation.lazy.LazyColumn
        import androidx.compose.foundation.lazy.items
        import androidx.compose.material3.Button
        import androidx.compose.material3.Card
        import androidx.compose.material3.CircularProgressIndicator
        import androidx.compose.material3.ExperimentalMaterial3Api
        import androidx.compose.material3.MaterialTheme
        import androidx.compose.material3.Scaffold
        import androidx.compose.material3.Text
        import androidx.compose.material3.TopAppBar
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.getValue
        import androidx.compose.ui.Alignment
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.unit.dp
        import androidx.hilt.navigation.compose.hiltViewModel
        import androidx.lifecycle.compose.collectAsStateWithLifecycle

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun MainScreen(
            viewModel: MainViewModel = hiltViewModel()
        ) {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("$appName") }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (val state = uiState) {
                        is MainUiState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        is MainUiState.Error -> {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error: ${'$'}{state.message}",
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { viewModel.refreshData() }) {
                                    Text("Retry")
                                }
                            }
                        }
                        is MainUiState.Success -> {
                            if (state.items.isEmpty()) {
                                Text(
                                    text = "No items available.",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    items(state.items, key = { it.id }) { item ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                    text = item.title,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = item.description,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    """.trimIndent()

    fun getColorKt(packageName: String): String = """
        package $packageName.ui.theme

        import androidx.compose.ui.graphics.Color

        val Purple80 = Color(0xFFD0BCFF)
        val PurpleGrey80 = Color(0xFFCCC2DC)
        val Pink80 = Color(0xFFEFB8C8)

        val Purple40 = Color(0xFF6650a4)
        val PurpleGrey40 = Color(0xFF625b71)
        val Pink40 = Color(0xFF7D5260)
    """.trimIndent()

    fun getTypeKt(packageName: String): String = """
        package $packageName.ui.theme

        import androidx.compose.material3.Typography
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.text.font.FontFamily
        import androidx.compose.ui.text.font.FontWeight
        import androidx.compose.ui.unit.sp

        val Typography = Typography(
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )
        )
    """.trimIndent()

    fun getThemeKt(packageName: String): String = """
        package $packageName.ui.theme

        import android.os.Build
        import androidx.compose.foundation.isSystemInDarkTheme
        import androidx.compose.material3.MaterialTheme
        import androidx.compose.material3.darkColorScheme
        import androidx.compose.material3.dynamicDarkColorScheme
        import androidx.compose.material3.dynamicLightColorScheme
        import androidx.compose.material3.lightColorScheme
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.platform.LocalContext

        private val DarkColorScheme = darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80
        )

        private val LightColorScheme = lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )

        @Composable
        fun AppTheme(
            darkTheme: Boolean = isSystemInDarkTheme(),
            dynamicColor: Boolean = true,
            content: @Composable () -> Unit
        ) {
            val colorScheme = when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    val context = LocalContext.current
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
                }
                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }

            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography,
                content = content
            )
        }
    """.trimIndent()

    fun getStringXml(appName: String): String = """
        <resources>
            <string name="app_name">$appName</string>
        </resources>
    """.trimIndent()

    fun getThemesXml(): String = """
        <resources>
            <style name="Theme.App" parent="android:Theme.Material.Light.NoActionBar" />
        </resources>
    """.trimIndent()

    // Sobreescribe el values-night/themes.xml del scaffold de Android Studio, que
    // referencia Theme.MaterialComponents (librería que esta plantilla no incluye)
    fun getThemesNightXml(): String = """
        <resources>
            <style name="Theme.App" parent="android:Theme.Material.NoActionBar" />
        </resources>
    """.trimIndent()

    // Sobreescribe el gradle.properties del scaffold, que no activa AndroidX
    fun getGradleProperties(): String = """
        org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
        org.gradle.configuration-cache=true
        kotlin.code.style=official
        # AndroidX (obligatorio para dependencias androidx.*)
        android.useAndroidX=true
        android.nonTransitiveRClass=true
    """.trimIndent()

    // Fija el wrapper a Gradle 8.13: el scaffold de Android Studio Quail pone 9.x,
    // incompatible con AGP 8.11 (el jar del wrapper descarga la versión indicada aquí)
    fun getGradleWrapperProperties(): String = """
        distributionBase=GRADLE_USER_HOME
        distributionPath=wrapper/dists
        distributionUrl=https\://services.gradle.org/distributions/gradle-8.13-bin.zip
        networkTimeout=10000
        validateDistributionUrl=true
        zipStoreBase=GRADLE_USER_HOME
        zipStorePath=wrapper/dists
    """.trimIndent()

    fun getGitIgnore(): String = """
        *.iml
        .gradle
        /build
        /.idea
        .DS_Store
        /captures
        .externalNativeBuild
        .cxx
        local.properties
    """.trimIndent()

    fun getReadme(appName: String, packageName: String): String = """
        # $appName

        Android project generated using **Android Studio Template (Hilt + Retrofit + Room)**.

        ## Architecture
        This project follows the official Google Android Architecture recommendations:
        - **UI Layer**: Jetpack Compose, Material3, ViewModels, `UiState` flow.
        - **Domain Layer**: UseCases encapsulating business logic.
        - **Data Layer**:
          - **Local**: Room Database, DAOs, Entities with KSP.
          - **Remote**: Retrofit, OkHttp Logging Interceptor, `kotlinx.serialization`.
          - **Repository**: Single source of truth coordinating local and remote sources.
        - **DI**: Dagger Hilt for dependency injection.

        ## Package Structure
        `$packageName`
        - `data/`: Repository implementations, DAOs, Database, Entities, API Service, DTOs
        - `di/`: Hilt modules (`DatabaseModule`, `NetworkModule`, `RepositoryModule`)
        - `domain/`: Business models, Repository interfaces, and UseCases
        - `ui/`: Compose Screens, ViewModels, UiState, Theme

    """.trimIndent()
}
