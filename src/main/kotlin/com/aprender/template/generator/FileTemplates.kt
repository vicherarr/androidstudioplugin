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
        navigationCompose = "2.9.1"
        # Tests: versiones alineadas con las corrutinas que resuelve el proyecto (1.8.1),
        # no con la última publicada (guías 41 y 42)
        junit = "4.13.2"
        coroutinesTest = "1.8.1"
        turbine = "1.1.0"

        [libraries]
        androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
        androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
        androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
        androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeKtx" }
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

        androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }

        retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
        retrofit-converter-kotlinx-serialization = { group = "com.squareup.retrofit2", name = "converter-kotlinx-serialization", version.ref = "retrofit" }
        okhttp-logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
        kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }

        room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
        room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

        junit = { group = "junit", name = "junit", version.ref = "junit" }
        kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutinesTest" }
        turbine = { group = "app.cash.turbine", name = "turbine", version.ref = "turbine" }

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
        import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
            }

            buildTypes {
                release {
                    // R8: elimina código muerto y ofusca; las reglas de Retrofit, Room y
                    // kotlinx.serialization están en proguard-rules.pro
                    isMinifyEnabled = true
                    isShrinkResources = true
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
            buildFeatures {
                compose = true
                buildConfig = true
            }
        }

        kotlin {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        room {
            schemaDirectory("${'$'}projectDir/schemas")
        }

        dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
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

            // Navigation (type-safe con kotlinx.serialization)
            implementation(libs.androidx.navigation.compose)

            // Retrofit & Serialization
            implementation(libs.retrofit)
            implementation(libs.retrofit.converter.kotlinx.serialization)
            implementation(libs.okhttp.logging.interceptor)
            implementation(libs.kotlinx.serialization.json)

            // Room
            implementation(libs.room.runtime)
            ksp(libs.room.compiler)

            // Tests unitarios (JVM): ViewModels y casos de uso sin emulador
            testImplementation(libs.junit)
            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    """.trimIndent()

    // Reglas R8 para las librerías que usan reflexión o generación de código.
    // kotlinx.serialization, Retrofit y Room ya aportan reglas de consumidor; aquí se
    // refuerzan los puntos que dependen del código de la app (DTOs y rutas @Serializable).
    fun getProguardRules(): String = """
        # --- kotlinx.serialization (DTOs y rutas de navegación type-safe) ---
        -keepattributes *Annotation*, InnerClasses, Signature
        -keepclassmembers class **${'$'}${'$'}serializer { *; }
        -if @kotlinx.serialization.Serializable class **
        -keepclassmembers class <1> {
            static <1>${'$'}Companion Companion;
            public static ** INSTANCE;
        }

        # --- Retrofit / OkHttp ---
        -keepattributes Exceptions
        -keep,allowobfuscation,allowshrinking interface retrofit2.Call
        -keep,allowobfuscation,allowshrinking class retrofit2.Response
        -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

        # --- Room ---
        -keep class * extends androidx.room.RoomDatabase { <init>(); }
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
        import $packageName.ui.navigation.AppNavHost
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
                            AppNavHost()
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

            @Query("SELECT * FROM items WHERE id = :id")
            fun getItemById(id: Int): Flow<ItemEntity?>

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
            fun getItem(id: Int): Flow<Item?>
            suspend fun refreshItems(): Result<Unit>
        }
    """.trimIndent()

    fun getDefaultItemRepositoryKt(packageName: String): String = """
        package $packageName.data.repository

        import $packageName.data.local.dao.ItemDao
        import $packageName.data.local.entity.ItemEntity
        import $packageName.data.remote.api.ApiService
        import $packageName.data.remote.dto.ItemDto
        import $packageName.di.IoDispatcher
        import $packageName.domain.repository.ItemRepository
        import $packageName.domain.model.Item
        import kotlinx.coroutines.CancellationException
        import kotlinx.coroutines.CoroutineDispatcher
        import kotlinx.coroutines.flow.Flow
        import kotlinx.coroutines.flow.flowOn
        import kotlinx.coroutines.flow.map
        import kotlinx.coroutines.withContext
        import javax.inject.Inject
        import javax.inject.Singleton

        /**
         * Única fuente de verdad de los items: la base de datos manda y la red solo
         * la refresca (offline-first).
         */
        @Singleton
        class DefaultItemRepository @Inject constructor(
            private val apiService: ApiService,
            private val itemDao: ItemDao,
            @IoDispatcher private val ioDispatcher: CoroutineDispatcher
        ) : ItemRepository {

            override fun getItems(): Flow<List<Item>> {
                return itemDao.getItems()
                    .map { entities -> entities.map { it.toDomain() } }
                    .flowOn(ioDispatcher)
            }

            override fun getItem(id: Int): Flow<Item?> {
                return itemDao.getItemById(id)
                    .map { entity -> entity?.toDomain() }
                    .flowOn(ioDispatcher)
            }

            override suspend fun refreshItems(): Result<Unit> = withContext(ioDispatcher) {
                try {
                    val remoteItems = apiService.getItems()
                    itemDao.insertItems(remoteItems.map { it.toEntity() })
                    Result.success(Unit)
                } catch (e: CancellationException) {
                    // La cancelación no es un error: debe propagarse para que la
                    // concurrencia estructurada funcione
                    throw e
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }

            private fun ItemEntity.toDomain(): Item =
                Item(id = id, title = title, description = description)

            private fun ItemDto.toEntity(): ItemEntity =
                ItemEntity(id = id, title = title, description = description)
        }
    """.trimIndent()

    // Dispatchers inyectados: permiten sustituirlos por un TestDispatcher en los tests
    // en lugar de acoplar el código a Dispatchers.IO
    fun getDispatcherModuleKt(packageName: String): String = """
        package $packageName.di

        import dagger.Module
        import dagger.Provides
        import dagger.hilt.InstallIn
        import dagger.hilt.components.SingletonComponent
        import kotlinx.coroutines.CoroutineDispatcher
        import kotlinx.coroutines.Dispatchers
        import javax.inject.Qualifier

        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class IoDispatcher

        @Module
        @InstallIn(SingletonComponent::class)
        object DispatcherModule {

            @Provides
            @IoDispatcher
            fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
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

        import $packageName.data.repository.DefaultItemRepository
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
                impl: DefaultItemRepository
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
        }
    """.trimIndent()

    fun getRefreshItemsUseCaseKt(packageName: String): String = """
        package $packageName.domain.usecase

        import $packageName.domain.repository.ItemRepository
        import javax.inject.Inject

        class RefreshItemsUseCase @Inject constructor(
            private val repository: ItemRepository
        ) {
            suspend operator fun invoke(): Result<Unit> {
                return repository.refreshItems()
            }
        }
    """.trimIndent()

    fun getGetItemUseCaseKt(packageName: String): String = """
        package $packageName.domain.usecase

        import $packageName.domain.repository.ItemRepository
        import $packageName.domain.model.Item
        import kotlinx.coroutines.flow.Flow
        import javax.inject.Inject

        class GetItemUseCase @Inject constructor(
            private val repository: ItemRepository
        ) {
            operator fun invoke(id: Int): Flow<Item?> {
                return repository.getItem(id)
            }
        }
    """.trimIndent()

    // Rutas type-safe: cada destino es una clase @Serializable (object si no lleva
    // argumentos, data class si los lleva). Sin strings ni claves de argumentos.
    fun getAppDestinationsKt(packageName: String): String = """
        package $packageName.ui.navigation

        import kotlinx.serialization.Serializable

        @Serializable
        data object HomeRoute

        @Serializable
        data class ItemDetailRoute(val itemId: Int)
    """.trimIndent()

    fun getAppNavHostKt(packageName: String): String = """
        package $packageName.ui.navigation

        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier
        import androidx.navigation.NavHostController
        import androidx.navigation.compose.NavHost
        import androidx.navigation.compose.composable
        import androidx.navigation.compose.rememberNavController
        import $packageName.ui.detail.DetailScreen
        import $packageName.ui.main.MainScreen

        @Composable
        fun AppNavHost(
            modifier: Modifier = Modifier,
            navController: NavHostController = rememberNavController()
        ) {
            NavHost(
                navController = navController,
                startDestination = HomeRoute,
                modifier = modifier
            ) {
                composable<HomeRoute> {
                    MainScreen(
                        onItemClick = { itemId ->
                            navController.navigate(ItemDetailRoute(itemId = itemId))
                        }
                    )
                }

                composable<ItemDetailRoute> {
                    // Los argumentos se leen en el ViewModel con SavedStateHandle.toRoute(),
                    // así la pantalla no depende del backStackEntry ni del NavController.
                    DetailScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    """.trimIndent()

    fun getDetailUiStateKt(packageName: String): String = """
        package $packageName.ui.detail

        import $packageName.domain.model.Item

        sealed interface DetailUiState {
            data object Loading : DetailUiState
            data object NotFound : DetailUiState
            data class Success(val item: Item) : DetailUiState
            data class Error(val message: String) : DetailUiState
        }
    """.trimIndent()

    fun getDetailViewModelKt(packageName: String): String = """
        package $packageName.ui.detail

        import androidx.lifecycle.SavedStateHandle
        import androidx.lifecycle.ViewModel
        import androidx.lifecycle.viewModelScope
        import androidx.navigation.toRoute
        import $packageName.domain.usecase.GetItemUseCase
        import $packageName.ui.navigation.ItemDetailRoute
        import dagger.hilt.android.lifecycle.HiltViewModel
        import kotlinx.coroutines.flow.SharingStarted
        import kotlinx.coroutines.flow.StateFlow
        import kotlinx.coroutines.flow.catch
        import kotlinx.coroutines.flow.map
        import kotlinx.coroutines.flow.stateIn
        import javax.inject.Inject

        @HiltViewModel
        class DetailViewModel @Inject constructor(
            savedStateHandle: SavedStateHandle,
            getItemUseCase: GetItemUseCase
        ) : ViewModel() {

            // Argumentos de navegación con tipado estricto, sin claves ni casts
            private val route: ItemDetailRoute = savedStateHandle.toRoute()

            val uiState: StateFlow<DetailUiState> = getItemUseCase(route.itemId)
                .map { item ->
                    if (item == null) DetailUiState.NotFound else DetailUiState.Success(item)
                }
                .catch { emit(DetailUiState.Error(it.message ?: "Unknown error")) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = DetailUiState.Loading
                )
        }
    """.trimIndent()

    fun getDetailScreenKt(packageName: String): String = """
        package $packageName.ui.detail

        import androidx.compose.foundation.layout.Box
        import androidx.compose.foundation.layout.Column
        import androidx.compose.foundation.layout.Spacer
        import androidx.compose.foundation.layout.fillMaxSize
        import androidx.compose.foundation.layout.height
        import androidx.compose.foundation.layout.padding
        import androidx.compose.material.icons.Icons
        import androidx.compose.material.icons.automirrored.filled.ArrowBack
        import androidx.compose.material3.CircularProgressIndicator
        import androidx.compose.material3.ExperimentalMaterial3Api
        import androidx.compose.material3.Icon
        import androidx.compose.material3.IconButton
        import androidx.compose.material3.MaterialTheme
        import androidx.compose.material3.Scaffold
        import androidx.compose.material3.Text
        import androidx.compose.material3.TopAppBar
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.getValue
        import androidx.compose.ui.Alignment
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.tooling.preview.Preview
        import androidx.compose.ui.unit.dp
        import androidx.hilt.navigation.compose.hiltViewModel
        import androidx.lifecycle.compose.collectAsStateWithLifecycle
        import $packageName.domain.model.Item
        import $packageName.ui.theme.AppTheme

        /** Pantalla con estado: conecta el ViewModel con el contenido sin estado. */
        @Composable
        fun DetailScreen(
            onNavigateBack: () -> Unit,
            modifier: Modifier = Modifier,
            viewModel: DetailViewModel = hiltViewModel()
        ) {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailContent(
                uiState = uiState,
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }

        /** Contenido sin estado: previsualizable y testeable en aislamiento. */
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun DetailContent(
            uiState: DetailUiState,
            onNavigateBack: () -> Unit,
            modifier: Modifier = Modifier
        ) {
            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        title = { Text("Detail") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (val state = uiState) {
                        is DetailUiState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        is DetailUiState.NotFound -> {
                            Text(
                                text = "Item not found.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        is DetailUiState.Error -> {
                            Text(
                                text = "Error: ${'$'}{state.message}",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        is DetailUiState.Success -> {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = state.item.title,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = state.item.description,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }

        @Preview(showBackground = true)
        @Composable
        private fun DetailContentSuccessPreview() {
            AppTheme {
                DetailContent(
                    uiState = DetailUiState.Success(
                        Item(id = 1, title = "First item", description = "A longer description")
                    ),
                    onNavigateBack = {}
                )
            }
        }

        @Preview(showBackground = true)
        @Composable
        private fun DetailContentNotFoundPreview() {
            AppTheme {
                DetailContent(uiState = DetailUiState.NotFound, onNavigateBack = {})
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
        import $packageName.domain.usecase.RefreshItemsUseCase
        import dagger.hilt.android.lifecycle.HiltViewModel
        import kotlinx.coroutines.flow.MutableSharedFlow
        import kotlinx.coroutines.flow.MutableStateFlow
        import kotlinx.coroutines.flow.SharedFlow
        import kotlinx.coroutines.flow.SharingStarted
        import kotlinx.coroutines.flow.StateFlow
        import kotlinx.coroutines.flow.asSharedFlow
        import kotlinx.coroutines.flow.catch
        import kotlinx.coroutines.flow.combine
        import kotlinx.coroutines.flow.stateIn
        import kotlinx.coroutines.launch
        import javax.inject.Inject

        @HiltViewModel
        class MainViewModel @Inject constructor(
            getItemsUseCase: GetItemsUseCase,
            private val refreshItemsUseCase: RefreshItemsUseCase
        ) : ViewModel() {

            private val loadError = MutableStateFlow<String?>(null)

            val uiState: StateFlow<MainUiState> =
                combine(getItemsUseCase(), loadError) { items, error ->
                    if (items.isEmpty() && error != null) {
                        MainUiState.Error(error)
                    } else {
                        MainUiState.Success(items)
                    }
                }
                    .catch { emit(MainUiState.Error(it.message ?: "Unknown error")) }
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5_000),
                        initialValue = MainUiState.Loading
                    )

            // Avisos de una sola vez (snackbar): un StateFlow los repetiría al recomponer
            private val _userMessages = MutableSharedFlow<String>()
            val userMessages: SharedFlow<String> = _userMessages.asSharedFlow()

            init {
                refresh()
            }

            fun refresh() {
                viewModelScope.launch {
                    refreshItemsUseCase()
                        .onSuccess { loadError.value = null }
                        .onFailure { error ->
                            val message = error.message ?: "Failed to load data"
                            val hasData = (uiState.value as? MainUiState.Success)?.items?.isNotEmpty() == true
                            // Con datos en pantalla el fallo es un aviso puntual;
                            // sin datos, es el estado de la pantalla
                            if (hasData) _userMessages.emit(message) else loadError.value = message
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
        import androidx.compose.material3.SnackbarHost
        import androidx.compose.material3.SnackbarHostState
        import androidx.compose.material3.Text
        import androidx.compose.material3.TopAppBar
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.LaunchedEffect
        import androidx.compose.runtime.getValue
        import androidx.compose.runtime.remember
        import androidx.compose.ui.Alignment
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.tooling.preview.Preview
        import androidx.compose.ui.unit.dp
        import androidx.hilt.navigation.compose.hiltViewModel
        import androidx.lifecycle.compose.collectAsStateWithLifecycle
        import $packageName.domain.model.Item
        import $packageName.ui.theme.AppTheme

        /** Pantalla con estado: conecta el ViewModel con el contenido sin estado. */
        @Composable
        fun MainScreen(
            onItemClick: (Int) -> Unit,
            modifier: Modifier = Modifier,
            viewModel: MainViewModel = hiltViewModel()
        ) {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(Unit) {
                viewModel.userMessages.collect { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }

            MainContent(
                uiState = uiState,
                onItemClick = onItemClick,
                onRetry = viewModel::refresh,
                snackbarHostState = snackbarHostState,
                modifier = modifier
            )
        }

        /** Contenido sin estado: previsualizable y testeable en aislamiento. */
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun MainContent(
            uiState: MainUiState,
            onItemClick: (Int) -> Unit,
            onRetry: () -> Unit,
            modifier: Modifier = Modifier,
            snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
        ) {
            Scaffold(
                modifier = modifier,
                snackbarHost = { SnackbarHost(snackbarHostState) },
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
                                Button(onClick = onRetry) {
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
                                            onClick = { onItemClick(item.id) },
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

        @Preview(showBackground = true)
        @Composable
        private fun MainContentSuccessPreview() {
            AppTheme {
                MainContent(
                    uiState = MainUiState.Success(
                        items = listOf(
                            Item(id = 1, title = "First item", description = "A short description"),
                            Item(id = 2, title = "Second item", description = "Another description")
                        )
                    ),
                    onItemClick = {},
                    onRetry = {}
                )
            }
        }

        @Preview(showBackground = true)
        @Composable
        private fun MainContentErrorPreview() {
            AppTheme {
                MainContent(
                    uiState = MainUiState.Error("No internet connection"),
                    onItemClick = {},
                    onRetry = {}
                )
            }
        }
    """.trimIndent()

    // ---------------------------------------------------------------------------
    // Tests unitarios (JVM). Patrón oficial: dispatcher de Main sustituido por regla
    // de JUnit, fakes solo en la frontera de datos y Turbine sobre los StateFlow.
    // ---------------------------------------------------------------------------

    fun getMainDispatcherRuleKt(packageName: String): String = """
        package $packageName.testutil

        import kotlinx.coroutines.Dispatchers
        import kotlinx.coroutines.ExperimentalCoroutinesApi
        import kotlinx.coroutines.test.TestDispatcher
        import kotlinx.coroutines.test.UnconfinedTestDispatcher
        import kotlinx.coroutines.test.resetMain
        import kotlinx.coroutines.test.setMain
        import org.junit.rules.TestWatcher
        import org.junit.runner.Description

        /**
         * viewModelScope vive en Dispatchers.Main, que no existe en la JVM: esta regla
         * lo sustituye por un dispatcher de test antes de cada caso y lo restaura después.
         */
        @OptIn(ExperimentalCoroutinesApi::class)
        class MainDispatcherRule(
            private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
        ) : TestWatcher() {

            override fun starting(description: Description) = Dispatchers.setMain(testDispatcher)

            override fun finished(description: Description) = Dispatchers.resetMain()
        }
    """.trimIndent()

    fun getFakeItemRepositoryKt(packageName: String): String = """
        package $packageName.testutil

        import $packageName.domain.model.Item
        import $packageName.domain.repository.ItemRepository
        import kotlinx.coroutines.flow.Flow
        import kotlinx.coroutines.flow.MutableStateFlow
        import kotlinx.coroutines.flow.map

        /**
         * Fake en la frontera de datos: los casos de uso reales se ejecutan encima, así
         * el test prueba la orquestación de verdad y no un ViewModel de mentira.
         */
        class FakeItemRepository : ItemRepository {

            val items = MutableStateFlow<List<Item>>(emptyList())

            /** El test decide qué devuelve el refresco (éxito o fallo). */
            var refreshResult: () -> Result<Unit> = { Result.success(Unit) }

            override fun getItems(): Flow<List<Item>> = items

            override fun getItem(id: Int): Flow<Item?> =
                items.map { list -> list.firstOrNull { it.id == id } }

            override suspend fun refreshItems(): Result<Unit> = refreshResult()
        }
    """.trimIndent()

    fun getMainViewModelTestKt(packageName: String): String = """
        package $packageName.ui.main

        import app.cash.turbine.test
        import $packageName.domain.model.Item
        import $packageName.domain.usecase.GetItemsUseCase
        import $packageName.domain.usecase.RefreshItemsUseCase
        import $packageName.testutil.FakeItemRepository
        import $packageName.testutil.MainDispatcherRule
        import kotlinx.coroutines.test.runTest
        import org.junit.Assert.assertEquals
        import org.junit.Rule
        import org.junit.Test
        import java.io.IOException

        class MainViewModelTest {

            @get:Rule
            val mainDispatcherRule = MainDispatcherRule()

            private val repository = FakeItemRepository()

            private fun viewModel() = MainViewModel(
                getItemsUseCase = GetItemsUseCase(repository),
                refreshItemsUseCase = RefreshItemsUseCase(repository)
            )

            @Test
            fun `exposes the items published by the repository`() = runTest {
                repository.items.value = listOf(ITEM)

                viewModel().uiState.test {
                    assertEquals(MainUiState.Success(listOf(ITEM)), expectMostRecentItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }

            @Test
            fun `shows an error state when the refresh fails and there is no data`() = runTest {
                repository.refreshResult = { Result.failure(IOException("No connection")) }

                viewModel().uiState.test {
                    assertEquals(MainUiState.Error("No connection"), expectMostRecentItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }

            @Test
            fun `keeps the data and warns the user when the refresh fails with data on screen`() = runTest {
                repository.items.value = listOf(ITEM)
                val viewModel = viewModel()

                viewModel.uiState.test {
                    assertEquals(MainUiState.Success(listOf(ITEM)), expectMostRecentItem())

                    repository.refreshResult = { Result.failure(IOException("No connection")) }
                    viewModel.userMessages.test {
                        viewModel.refresh()
                        assertEquals("No connection", awaitItem())
                    }

                    // La lista sigue en pantalla: el fallo no la sustituye por un error
                    expectNoEvents()
                    cancelAndIgnoreRemainingEvents()
                }
            }

            @Test
            fun `reacts to new data without any user action`() = runTest {
                viewModel().uiState.test {
                    assertEquals(MainUiState.Success(emptyList()), expectMostRecentItem())

                    repository.items.value = listOf(ITEM)

                    assertEquals(MainUiState.Success(listOf(ITEM)), awaitItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }

            private companion object {
                val ITEM = Item(id = 1, title = "Title", description = "Description")
            }
        }
    """.trimIndent()

    /**
     * El detalle se prueba a la altura del caso de uso, no del ViewModel:
     * `SavedStateHandle.toRoute()` decodifica la ruta con un `android.os.Bundle` real,
     * que no existe en la JVM. Ese trozo corresponde a un test instrumentado.
     */
    fun getGetItemUseCaseTestKt(packageName: String): String = """
        package $packageName.domain.usecase

        import app.cash.turbine.test
        import $packageName.domain.model.Item
        import $packageName.testutil.FakeItemRepository
        import kotlinx.coroutines.test.runTest
        import org.junit.Assert.assertEquals
        import org.junit.Assert.assertNull
        import org.junit.Test

        class GetItemUseCaseTest {

            private val repository = FakeItemRepository()
            private val getItem = GetItemUseCase(repository)

            @Test
            fun `emits the item matching the requested id`() = runTest {
                repository.items.value = listOf(FIRST, SECOND)

                getItem(id = 2).test {
                    assertEquals(SECOND, awaitItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }

            @Test
            fun `emits null when no item has that id`() = runTest {
                repository.items.value = listOf(FIRST)

                getItem(id = 99).test {
                    assertNull(awaitItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }

            @Test
            fun `re-emits when the stored items change`() = runTest {
                getItem(id = 1).test {
                    assertNull(awaitItem())

                    repository.items.value = listOf(FIRST)

                    assertEquals(FIRST, awaitItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }

            private companion object {
                val FIRST = Item(id = 1, title = "First", description = "First description")
                val SECOND = Item(id = 2, title = "Second", description = "Second description")
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
        - **Navigation**: Navigation Compose type-safe (`@Serializable` routes).
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
        - `ui/navigation/`: `AppDestinations` (rutas `@Serializable`) and `AppNavHost` (nav graph)

        ## Navigation (type-safe)
        Routes are Kotlin types, not strings — the compiler checks every destination and argument.

        1. Declare the destination in `ui/navigation/AppDestinations.kt`:
           `@Serializable data class ItemDetailRoute(val itemId: Int)`
        2. Register it in `ui/navigation/AppNavHost.kt`:
           `composable<ItemDetailRoute> { DetailScreen(onNavigateBack = { navController.popBackStack() }) }`
        3. Navigate with an instance: `navController.navigate(ItemDetailRoute(itemId = 42))`
        4. Read the arguments in the ViewModel: `savedStateHandle.toRoute<ItemDetailRoute>()`

        Screens never receive the `NavController`: they expose lambdas (`onItemClick`,
        `onNavigateBack`) and the `NavHost` decides where each event leads. This keeps the
        screens previewable, testable, and reusable.

        ## Tests

        ```bash
        ./gradlew testDebugUnitTest
        ```

        JVM only — no emulator. The suite uses JUnit 4, `kotlinx-coroutines-test` and
        Turbine, with `MainDispatcherRule` replacing `Dispatchers.Main` and
        `FakeItemRepository` as the only fake: the use cases under test are the real ones,
        so what gets verified is the actual orchestration.

        > `DetailViewModel` is not covered here on purpose. `SavedStateHandle.toRoute()`
        > decodes the route through a real `android.os.Bundle`, which does not exist in
        > plain JVM tests, so that piece belongs in an instrumented test. Its logic is
        > covered one layer below, in `GetItemUseCaseTest`.

    """.trimIndent()
}
