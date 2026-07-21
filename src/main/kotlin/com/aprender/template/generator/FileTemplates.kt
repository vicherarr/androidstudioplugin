package com.aprender.template.generator

object FileTemplates {

    fun getRootBuildGradleKts(): String = """
        // Fichero de build RAÍZ del proyecto.
        // Aquí solo se DECLARAN los plugins que estarán disponibles para los módulos.
        // "apply false" significa: descárgalo, pero no lo actives en este fichero; cada
        // módulo (aquí solo :app) decide cuáles usa de verdad.
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
        // Fichero que define QUÉ MÓDULOS forman el proyecto y DE DÓNDE se descargan
        // las librerías. Gradle lo lee antes que cualquier build.gradle.kts.

        // Repositorios para buscar los PLUGINS de Gradle
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
        // Repositorios para buscar las LIBRERÍAS (dependencias)
        dependencyResolutionManagement {
            // Obliga a declarar los repositorios aquí y no en cada módulo: así todos
            // los módulos descargan de los mismos sitios
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                google()        // librerías de Android y Google
                mavenCentral()  // el resto (Retrofit, OkHttp, Turbine...)
            }
        }

        rootProject.name = "$appName"
        include(":app")   // el proyecto tiene un único módulo, llamado "app"
    """.trimIndent()

    fun getLibsVersionsToml(): String = """
        # CATÁLOGO DE VERSIONES (version catalog).
        # Es la lista central de librerías del proyecto. En vez de escribir la versión
        # en cada build.gradle.kts, se define aquí una vez y se referencia con
        # libs.nombre.de.la.libreria. Actualizar una versión = tocar una sola línea.
        #
        # [versions]  -> los números de versión
        # [libraries] -> las librerías, que apuntan a una versión con version.ref
        # [plugins]   -> los plugins de Gradle

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

        // Build del módulo :app — aquí se activan los plugins y se declaran las librerías
        plugins {
            alias(libs.plugins.android.application)   // convierte el módulo en una app Android
            alias(libs.plugins.kotlin.android)        // permite escribir Kotlin
            alias(libs.plugins.kotlin.compose)        // compilador de Jetpack Compose
            alias(libs.plugins.kotlin.serialization)  // convierte clases <-> JSON (y rutas type-safe)
            alias(libs.plugins.ksp)                   // genera código en compilación (Hilt y Room lo usan)
            alias(libs.plugins.hilt.android)          // inyección de dependencias
            alias(libs.plugins.room)                  // soporte de base de datos Room
        }

        android {
            // namespace: el paquete base del código (el de los import y el de R)
            namespace = "$packageName"
            // compileSdk: versión de Android con la que se COMPILA (usa las APIs más nuevas)
            compileSdk = 36

            defaultConfig {
                // applicationId: identificador único de la app en el dispositivo y en Google Play
                applicationId = "$packageName"
                // minSdk: versión MÍNIMA de Android donde se puede instalar (26 = Android 8)
                minSdk = 26
                // targetSdk: versión para la que está probada; afecta a permisos y comportamiento
                targetSdk = 36
                versionCode = 1     // número interno que sube en cada publicación
                versionName = "1.0" // versión visible para el usuario
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
            // Versión de Java que entiende el código compilado
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            buildFeatures {
                compose = true      // activa Jetpack Compose
                buildConfig = true  // genera la clase BuildConfig (se usa para saber si es debug)
            }
        }

        // Misma versión de Java para el compilador de Kotlin
        kotlin {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        room {
            // Room exporta el esquema de la BD a JSON en la carpeta schemas/.
            // Sirve para escribir migraciones cuando cambie la base de datos.
            schemaDirectory("${'$'}projectDir/schemas")
        }

        // Librerías del módulo. Vienen del catálogo gradle/libs.versions.toml
        //   implementation      -> disponible en la app
        //   ksp                 -> generador de código (no acaba dentro de la app)
        //   debugImplementation -> solo en las builds de depuración
        //   testImplementation  -> solo al ejecutar los tests
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
        <!--
            El MANIFEST es la ficha de identidad de la app para el sistema Android:
            qué permisos necesita, qué pantallas tiene y cuál se abre al pulsar el icono.
        -->
        <manifest xmlns:android="http://schemas.android.com/apk/res/android">

            <!-- Sin este permiso, cualquier llamada de red falla -->
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
                    <!--
                        Este intent-filter marca la pantalla de ENTRADA: MAIN dice que es
                        el punto de arranque y LAUNCHER que aparezca en el menú de apps.
                    -->
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

        /**
         * Clase Application: es lo PRIMERO que se crea al abrir la app y vive mientras
         * la app viva.
         *
         * @HiltAndroidApp le dice a Hilt que genere aquí el "contenedor" de dependencias
         * de toda la app. Sin esta anotación, ningún @Inject funcionaría.
         * Está registrada en el AndroidManifest.xml con android:name=".MainApplication".
         */
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

        /**
         * Única Activity de la app: hace de contenedor y no contiene lógica.
         * Todas las "pantallas" son composables dentro del grafo de navegación.
         *
         * @AndroidEntryPoint permite que Hilt inyecte dependencias aquí dentro
         * (por ejemplo, los ViewModel que piden las pantallas).
         */
        @AndroidEntryPoint
        class MainActivity : ComponentActivity() {

            // onCreate se ejecuta al crear la pantalla (y al girar el móvil, por ejemplo)
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                // Dibuja debajo de la barra de estado y la de navegación
                enableEdgeToEdge()
                // setContent: a partir de aquí la interfaz se describe con Compose
                setContent {
                    // AppTheme aplica colores, tipografías y modo oscuro a todo lo de dentro
                    AppTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            // El grafo de navegación decide qué pantalla se ve
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

        /**
         * ENTITY = una fila de la base de datos, representada como clase de Kotlin.
         * Room crea la tabla "items" a partir de esta clase: cada propiedad es una columna.
         *
         * Ojo: esta clase pertenece a la capa de DATOS. La pantalla nunca la usa
         * directamente; para eso está el modelo de dominio (domain/model/Item.kt).
         */
        @Entity(tableName = "items")
        data class ItemEntity(
            @PrimaryKey val id: Int,  // clave primaria: identifica la fila y no se repite
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

        /**
         * DAO (Data Access Object) = las operaciones permitidas sobre la tabla.
         * Solo se declara QUÉ se quiere hacer; Room genera el código que lo hace.
         *
         * Dos formas de devolver datos:
         *  - Flow<T>: consulta OBSERVABLE. Room reemite solo cuando la tabla cambia,
         *    así la pantalla se actualiza sola. No necesita suspend.
         *  - suspend fun: operación puntual que tarda (insertar, borrar) y por eso
         *    debe ejecutarse en una corrutina, nunca bloqueando la pantalla.
         */
        @Dao
        interface ItemDao {

            @Query("SELECT * FROM items ORDER BY createdAt DESC")
            fun getItems(): Flow<List<ItemEntity>>

            // :id en el SQL se sustituye por el parámetro id de la función
            @Query("SELECT * FROM items WHERE id = :id")
            fun getItemById(id: Int): Flow<ItemEntity?>   // null si no existe esa fila

            // REPLACE: si ya existe una fila con esa clave primaria, la sobreescribe
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

        /**
         * La base de datos: une las tablas (entities) con sus DAOs.
         *
         * version = 1 -> si algún día cambias una tabla, hay que subir este número y
         * escribir una MIGRACIÓN, o la app fallará al abrir bases de datos antiguas.
         * exportSchema = true -> guarda el esquema en schemas/ para poder migrar.
         *
         * Es abstract porque Room genera la implementación real al compilar.
         */
        @Database(entities = [ItemEntity::class], version = 1, exportSchema = true)
        abstract class AppDatabase : RoomDatabase() {
            abstract fun itemDao(): ItemDao
        }
    """.trimIndent()

    fun getItemDtoKt(packageName: String): String = """
        package $packageName.data.remote.dto

        import kotlinx.serialization.SerialName
        import kotlinx.serialization.Serializable

        /**
         * DTO (Data Transfer Object) = cómo viene el JSON del servidor, tal cual.
         *
         * @Serializable permite convertir el JSON en esta clase automáticamente.
         * @SerialName conecta el nombre del campo en el JSON con el nombre en Kotlin:
         * el servidor manda "body", pero en el código se llama description, que se
         * entiende mejor. Separar DTO de Entity evita que un cambio en la API te
         * obligue a tocar la base de datos.
         */
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

        /**
         * Definición de la API REST. Solo se describe la llamada; Retrofit escribe el
         * código que abre la conexión, lee la respuesta y la convierte a objetos.
         *
         * @GET("posts") se pega detrás de la BASE_URL del NetworkModule.
         * suspend: la llamada tarda, así que solo puede hacerse desde una corrutina.
         */
        interface ApiService {
            @GET("posts")
            suspend fun getItems(): List<ItemDto>
        }
    """.trimIndent()

    fun getItemKt(packageName: String): String = """
        package $packageName.domain.model

        /**
         * MODELO DE DOMINIO: el item tal y como lo entiende la app, sin saber nada de
         * dónde viene (ni Room, ni Retrofit, ni JSON).
         *
         * Por eso no tiene anotaciones: es Kotlin puro. Es lo que viaja hacia la UI.
         */
        data class Item(
            val id: Int,
            val title: String,
            val description: String
        )
    """.trimIndent()

    fun getItemRepositoryKt(packageName: String): String = """
        package $packageName.data.repository

        import $packageName.domain.model.Item
        import kotlinx.coroutines.flow.Flow

        /**
         * CONTRATO del repositorio: qué se puede pedir sobre los items, sin decir cómo
         * se consigue. La implementación es DefaultItemRepository, en este mismo paquete.
         *
         * ¿Por qué una interfaz? Porque quien la usa (los casos de uso) no depende de
         * Room ni de Retrofit, y en los tests se puede sustituir por una versión falsa.
         *
         * Vive en la capa de DATOS, no en domain: según la arquitectura recomendada,
         * el repositorio pertenece a la capa que es dueña de los datos, y el dominio
         * (los casos de uso) depende de él.
         */
        interface ItemRepository {

            /** Lista observable: emite de nuevo cada vez que cambian los datos guardados. */
            fun getItems(): Flow<List<Item>>

            /** Un item concreto; emite null si no existe. */
            fun getItem(id: Int): Flow<Item?>

            /**
             * Pide datos nuevos a la red y los guarda.
             * Devuelve Result para poder informar del fallo sin lanzar excepciones.
             */
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
         * Implementación del repositorio (patrón OFFLINE-FIRST).
         *
         * La regla: la pantalla SIEMPRE lee de la base de datos, nunca de la red.
         * La red solo sirve para actualizar la base de datos. Ventajas: la app funciona
         * sin conexión y hay una única fuente de verdad, así que no puede haber dos
         * versiones distintas del mismo dato.
         *
         *   Red  --refreshItems()-->  Room  --getItems()-->  UI
         *
         * @Inject constructor: Hilt sabe crear esta clase y le pasa solo lo que pide.
         * @Singleton: existe una única instancia en toda la app.
         */
        @Singleton
        class DefaultItemRepository @Inject constructor(
            private val apiService: ApiService,
            private val itemDao: ItemDao,
            // El dispatcher se inyecta en vez de escribir Dispatchers.IO a pelo: así en
            // los tests se puede sustituir por uno de prueba
            @IoDispatcher private val ioDispatcher: CoroutineDispatcher
        ) : ItemRepository {

            override fun getItems(): Flow<List<Item>> {
                return itemDao.getItems()
                    // map transforma cada emisión: de entidades de BD a modelos de dominio
                    .map { entities -> entities.map { it.toDomain() } }
                    // flowOn mueve el trabajo de ARRIBA a un hilo secundario, para no
                    // congelar la pantalla
                    .flowOn(ioDispatcher)
            }

            override fun getItem(id: Int): Flow<Item?> {
                return itemDao.getItemById(id)
                    .map { entity -> entity?.toDomain() }
                    .flowOn(ioDispatcher)
            }

            override suspend fun refreshItems(): Result<Unit> = withContext(ioDispatcher) {
                try {
                    // 1. Pedir los datos a la API
                    val remoteItems = apiService.getItems()
                    // 2. Guardarlos en la base de datos. No hace falta avisar a la
                    //    pantalla: el Flow del DAO reemite solo y la UI se actualiza
                    itemDao.insertItems(remoteItems.map { it.toEntity() })
                    Result.success(Unit)
                } catch (e: CancellationException) {
                    // OJO: si el usuario sale de la pantalla, la corrutina se cancela
                    // lanzando esta excepción. NO es un error, hay que dejarla pasar o
                    // se rompe la cancelación de corrutinas.
                    throw e
                } catch (e: Exception) {
                    // Sin internet, servidor caído, JSON inesperado... el fallo se
                    // devuelve envuelto en Result en vez de tirar la app abajo
                    Result.failure(e)
                }
            }

            // MAPPERS: traducen entre las capas. Son privados porque solo importan aquí.
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

        /**
         * QUALIFIER: una etiqueta para distinguir objetos del mismo tipo.
         * Hay varios CoroutineDispatcher posibles (IO, Default, Main); esta anotación
         * indica cuál se quiere pedir.
         */
        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class IoDispatcher

        /**
         * Provee el dispatcher de entrada/salida (red y disco).
         *
         * ¿Por qué no escribir Dispatchers.IO directamente donde haga falta? Porque
         * entonces el código quedaría atado al hilo real y los tests serían lentos e
         * impredecibles. Inyectándolo, un test puede pasar un dispatcher de prueba.
         */
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

        /**
         * MÓDULO DE HILT: receta para construir objetos que no se pueden crear con
         * @Inject constructor, normalmente porque vienen de una librería.
         *
         * @InstallIn(SingletonComponent::class): lo que se cree aquí vive mientras
         * viva la app.
         */
        @Module
        @InstallIn(SingletonComponent::class)
        object DatabaseModule {

            /**
             * @Provides: "cuando alguien pida un AppDatabase, constrúyelo así".
             * @Singleton: créalo UNA sola vez y reutilízalo. Abrir varias conexiones a
             * la misma base de datos sería un error caro.
             */
            @Provides
            @Singleton
            fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
                return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "$dbName"   // nombre del fichero de la base de datos en el dispositivo
                ).build()
            }

            // El DAO se saca de la base de datos ya creada: Hilt inyecta el AppDatabase
            // de arriba automáticamente al ver el parámetro
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

        /**
         * Módulo con todo lo necesario para hablar con la API.
         * Se construye por piezas, y cada una recibe la anterior:
         *
         *   Json + OkHttpClient  ->  Retrofit  ->  ApiService
         */
        @Module
        @InstallIn(SingletonComponent::class)
        object NetworkModule {

            // Dirección base del servidor; los @GET("...") se pegan detrás
            private const val BASE_URL = "$baseUrl"

            /** Conversor de JSON, configurado para ser tolerante con respuestas raras. */
            @Provides
            @Singleton
            fun provideJson(): Json = Json {
                // Si el servidor manda campos que la app no conoce, los ignora en vez
                // de fallar (muy habitual cuando la API evoluciona)
                ignoreUnknownKeys = true
                // Si llega null donde se esperaba un valor, usa el valor por defecto
                coerceInputValues = true
            }

            /** Cliente HTTP: quien de verdad hace las peticiones de red. */
            @Provides
            @Singleton
            fun provideOkHttpClient(): OkHttpClient {
                return OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        // En debug se registra el contenido de cada petición para poder
                        // depurar; en release NO, porque el log podría filtrar datos
                        // personales o tokens
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    // Sin timeouts, una red lenta dejaría la petición colgada para siempre
                    .connectTimeout(15, TimeUnit.SECONDS)  // abrir la conexión
                    .readTimeout(15, TimeUnit.SECONDS)     // recibir la respuesta
                    .writeTimeout(15, TimeUnit.SECONDS)    // enviar los datos
                    .build()
            }

            /** Retrofit une el cliente HTTP con el conversor de JSON. */
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

            /** Implementación de la interfaz ApiService, generada por Retrofit. */
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
        import $packageName.data.repository.ItemRepository
        import dagger.Binds
        import dagger.Module
        import dagger.hilt.InstallIn
        import dagger.hilt.components.SingletonComponent
        import javax.inject.Singleton

        /**
         * Conecta la INTERFAZ con su IMPLEMENTACIÓN: cuando alguien pida un
         * ItemRepository, Hilt le entregará un DefaultItemRepository.
         *
         * Se usa @Binds en vez de @Provides porque la clase ya sabe construirse sola
         * (tiene @Inject constructor); aquí solo se indica la equivalencia. Por eso el
         * módulo es abstract y la función no tiene cuerpo: genera menos código.
         */
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

        import $packageName.data.repository.ItemRepository
        import $packageName.domain.model.Item
        import kotlinx.coroutines.flow.Flow
        import javax.inject.Inject

        /**
         * CASO DE USO = una acción concreta que la app sabe hacer, con nombre de verbo.
         * Aquí: "obtener los items".
         *
         * Regla: un caso de uso, una sola operación. Al declararla con
         * "operator fun invoke", se llama como si fuera una función:
         *
         *     getItemsUseCase()    en vez de    getItemsUseCase.execute()
         */
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

        import $packageName.data.repository.ItemRepository
        import javax.inject.Inject

        /**
         * Caso de uso "refrescar los items": pide datos nuevos a la red.
         *
         * Es suspend porque tarda. Está separado de GetItemsUseCase a propósito:
         * obtener y refrescar son dos acciones distintas.
         */
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

        import $packageName.data.repository.ItemRepository
        import $packageName.domain.model.Item
        import kotlinx.coroutines.flow.Flow
        import javax.inject.Inject

        /**
         * Caso de uso "obtener un item por su id", que usa la pantalla de detalle.
         * Devuelve un Flow que emite null si ese item no existe.
         */
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

        /*
         * DESTINOS DE NAVEGACIÓN (type-safe).
         *
         * Cada pantalla es una clase, no un texto tipo "detalle/{id}". Ventaja: si te
         * equivocas en el nombre o en el tipo de un argumento, no compila. Con strings,
         * el error aparecía al pulsar el botón en el móvil.
         *
         * @Serializable permite convertir la ruta a datos y recuperarla después.
         */

        /** Pantalla de inicio. Es un object porque no necesita argumentos. */
        @Serializable
        data object HomeRoute

        /** Pantalla de detalle. Es data class porque viaja con el id del item. */
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

        /**
         * GRAFO DE NAVEGACIÓN: el mapa de la app. Dice qué pantallas existen y cómo se
         * pasa de una a otra.
         *
         * Fíjate en que las pantallas reciben LAMBDAS (onItemClick, onNavigateBack) y
         * nunca el navController. Así una pantalla no sabe a dónde lleva su botón: eso
         * se decide aquí. Eso las hace previsualizables y reutilizables.
         */
        @Composable
        fun AppNavHost(
            modifier: Modifier = Modifier,
            // rememberNavController crea el "mando" de navegación y lo conserva
            // aunque la pantalla se redibuje
            navController: NavHostController = rememberNavController()
        ) {
            NavHost(
                navController = navController,
                startDestination = HomeRoute,   // pantalla que se ve al abrir la app
                modifier = modifier
            ) {
                // composable<Ruta> { } asocia una ruta con la pantalla que se dibuja
                composable<HomeRoute> {
                    MainScreen(
                        onItemClick = { itemId ->
                            // Navegar = crear la ruta con sus argumentos. El compilador
                            // exige que el itemId sea un Int, no hay strings de por medio
                            navController.navigate(ItemDetailRoute(itemId = itemId))
                        }
                    )
                }

                composable<ItemDetailRoute> {
                    // Los argumentos se leen en el ViewModel con SavedStateHandle.toRoute(),
                    // así la pantalla no depende del backStackEntry ni del NavController.
                    DetailScreen(
                        // popBackStack quita la pantalla actual de la pila y vuelve a
                        // la anterior: es el "atrás" de toda la vida
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    """.trimIndent()

    fun getDetailUiStateKt(packageName: String): String = """
        package $packageName.ui.detail

        import $packageName.domain.model.Item

        /** Estados posibles de la pantalla de detalle (ver MainUiState para el porqué). */
        sealed interface DetailUiState {
            data object Loading : DetailUiState
            /** El id de la ruta no corresponde a ningún item guardado. */
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

        /**
         * VIEWMODEL del detalle.
         *
         * Lo interesante: no recibe el id por parámetro, lo saca de la navegación.
         * SavedStateHandle es la "caja" donde Android guarda los argumentos con los que
         * se abrió la pantalla (y que sobrevive si el sistema mata la app por memoria).
         */
        @HiltViewModel
        class DetailViewModel @Inject constructor(
            savedStateHandle: SavedStateHandle,
            getItemUseCase: GetItemUseCase
        ) : ViewModel() {

            // toRoute() reconstruye la ruta completa con sus argumentos ya tipados.
            // Nada de savedStateHandle.get<Int>("itemId"): si el nombre o el tipo no
            // cuadran, falla al compilar en vez de en el móvil del usuario.
            private val route: ItemDetailRoute = savedStateHandle.toRoute()

            val uiState: StateFlow<DetailUiState> = getItemUseCase(route.itemId)
                .map { item ->
                    // El caso de uso devuelve null cuando no existe; aquí se traduce
                    // a un estado que la pantalla sabe pintar
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

        /**
         * Pantalla de detalle con estado. Igual que MainScreen: habla con el ViewModel
         * y delega el dibujo en el composable sin estado de abajo.
         *
         * No recibe el itemId: ese dato lo recoge el ViewModel de la navegación.
         */
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

        /** Contenido sin estado del detalle: recibe qué pintar y avisa del "atrás". */
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
                                    // AutoMirrored: la flecha se gira sola en idiomas
                                    // que se leen de derecha a izquierda (árabe, hebreo)
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    // contentDescription lo lee el lector de pantalla
                                    // para personas ciegas: nunca lo dejes vacío
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

        /**
         * ESTADO DE LA PANTALLA: todo lo que la lista necesita para dibujarse.
         *
         * Es una "sealed interface": la pantalla solo puede estar en UNO de estos
         * estados, y el compilador obliga a tratarlos todos en el when. Así es
         * imposible olvidarse de pintar el error o el cargando.
         */
        sealed interface MainUiState {
            /** Aún no hay datos: se muestra el círculo de progreso. */
            data object Loading : MainUiState

            /** Hay datos (la lista puede estar vacía, y eso también se pinta). */
            data class Success(val items: List<Item>) : MainUiState

            /** No se pudo cargar nada: se muestra el mensaje y el botón de reintentar. */
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

        /**
         * VIEWMODEL de la lista.
         *
         * Su trabajo: preparar el estado que la pantalla pinta y recibir los eventos
         * del usuario. NO sabe nada de Compose, ni de Room, ni de Retrofit.
         *
         * Sobrevive a los giros de pantalla, así que los datos no se recargan al rotar.
         * @HiltViewModel permite que Hilt le inyecte los casos de uso.
         */
        @HiltViewModel
        class MainViewModel @Inject constructor(
            getItemsUseCase: GetItemsUseCase,
            private val refreshItemsUseCase: RefreshItemsUseCase
        ) : ViewModel() {

            // Mensaje de error de la última carga; null significa "sin error"
            private val loadError = MutableStateFlow<String?>(null)

            /**
             * El estado de la pantalla, en un único flujo observable.
             *
             * combine: mezcla dos flujos (los items y el error) y produce uno nuevo
             * cada vez que cualquiera de los dos cambia.
             */
            val uiState: StateFlow<MainUiState> =
                combine(getItemsUseCase(), loadError) { items, error ->
                    // Solo se muestra la pantalla de error si NO hay nada que enseñar
                    if (items.isEmpty() && error != null) {
                        MainUiState.Error(error)
                    } else {
                        MainUiState.Success(items)
                    }
                }
                    // catch: si el flujo de datos revienta, la app no cae; se pinta el error
                    .catch { emit(MainUiState.Error(it.message ?: "Unknown error")) }
                    // stateIn convierte el flujo en un StateFlow, que siempre tiene un
                    // valor actual disponible para la pantalla
                    .stateIn(
                        scope = viewModelScope,  // se cancela solo al destruirse el ViewModel
                        // WhileSubscribed(5_000): trabaja mientras la pantalla mira, y
                        // sigue 5 segundos más. Así un giro de pantalla no reinicia la
                        // carga, pero tampoco se gastan recursos en segundo plano.
                        started = SharingStarted.WhileSubscribed(5_000),
                        initialValue = MainUiState.Loading  // lo que se ve al principio
                    )

            /**
             * Avisos de UNA SOLA VEZ (un snackbar). No van en el uiState porque un
             * estado se vuelve a leer al recomponer o al girar, y el mensaje saldría
             * una y otra vez. Un SharedFlow se consume una vez y ya está.
             */
            private val _userMessages = MutableSharedFlow<String>()
            val userMessages: SharedFlow<String> = _userMessages.asSharedFlow()

            // init se ejecuta al crear el ViewModel: primera carga desde la red
            init {
                refresh()
            }

            /** Evento del usuario: reintentar o refrescar. */
            fun refresh() {
                // launch arranca una corrutina; si el ViewModel muere, se cancela sola
                viewModelScope.launch {
                    refreshItemsUseCase()
                        .onSuccess { loadError.value = null }
                        .onFailure { error ->
                            val message = error.message ?: "Failed to load data"
                            val hasData = (uiState.value as? MainUiState.Success)?.items?.isNotEmpty() == true
                            // Con datos en pantalla, el fallo es un aviso puntual y la
                            // lista se queda. Sin datos, el fallo ES la pantalla.
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

        /**
         * PANTALLA CON ESTADO (stateful): habla con el ViewModel y le pasa los datos ya
         * masticados al composable de abajo. Es la única parte que conoce Hilt.
         *
         * La pantalla se divide en dos a propósito: esta parte no se puede previsualizar
         * (necesita un ViewModel real), pero MainContent sí.
         */
        @Composable
        fun MainScreen(
            onItemClick: (Int) -> Unit,
            modifier: Modifier = Modifier,
            // hiltViewModel() pide el ViewModel a Hilt, ya construido con sus casos de uso
            viewModel: MainViewModel = hiltViewModel()
        ) {
            // collectAsStateWithLifecycle escucha el flujo del ViewModel y redibuja al
            // cambiar. "WithLifecycle" = deja de escuchar si la pantalla no está visible,
            // para no gastar batería.
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            // remember conserva el objeto entre redibujados; sin él se crearía uno nuevo
            // en cada recomposición y el snackbar desaparecería
            val snackbarHostState = remember { SnackbarHostState() }

            // LaunchedEffect lanza una corrutina ligada a la pantalla. Con Unit como
            // clave, se ejecuta una sola vez, al entrar. Aquí escucha los avisos del
            // ViewModel y los muestra como snackbar.
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

        /**
         * CONTENIDO SIN ESTADO (stateless): recibe el estado ya calculado y devuelve
         * los clics hacia arriba mediante lambdas. No guarda nada ni conoce el ViewModel.
         *
         * Este es el patrón "state down, events up" de Compose: el estado baja, los
         * eventos suben. Un composable así se puede previsualizar y probar solo.
         */
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun MainContent(
            uiState: MainUiState,
            onItemClick: (Int) -> Unit,
            onRetry: () -> Unit,
            // Modifier siempre como parámetro y con valor por defecto: permite a quien
            // use el composable decidir tamaño, márgenes o posición desde fuera
            modifier: Modifier = Modifier,
            snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
        ) {
            // Scaffold monta la estructura típica de Material3: barra superior, hueco
            // para snackbars y el contenido
            Scaffold(
                modifier = modifier,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    TopAppBar(
                        title = { Text("$appName") }
                    )
                }
            ) { innerPadding ->
                // innerPadding es el espacio que ocupan las barras; hay que aplicarlo o
                // el contenido quedará por debajo de ellas
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // El when sobre el estado decide QUÉ se pinta. Al ser una sealed
                    // interface, el compilador avisa si falta algún caso.
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
                            // "Hay datos" incluye el caso de lista vacía, que merece su
                            // propio mensaje y no un error
                            if (state.items.isEmpty()) {
                                Text(
                                    text = "No items available.",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else {
                                // LazyColumn = lista que solo crea en memoria los
                                // elementos visibles (el equivalente a RecyclerView)
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    // key: identifica cada fila para que Compose sepa
                                    // cuál es cuál al cambiar la lista y no redibuje de más
                                    items(state.items, key = { it.id }) { item ->
                                        Card(
                                            // Card con onClick da la animación al pulsar
                                            // y el rol de accesibilidad correctos
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

        /*
         * PREVIEWS: se ven en el panel Design de Android Studio sin lanzar la app.
         * Solo son posibles porque MainContent no depende del ViewModel: basta con
         * inventarse un estado y pasárselo.
         */

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
        import $packageName.data.repository.ItemRepository
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

        /**
         * Tests del MainViewModel. Se ejecutan en la JVM (segundos, sin emulador) con:
         *
         *   ./gradlew testDebugUnitTest
         *
         * Fíjate en que Hilt no aparece por ningún lado: como las dependencias se piden
         * por constructor, el test las pasa a mano. Eso es una señal de buena arquitectura.
         */
        class MainViewModelTest {

            // La regla instala un dispatcher de prueba como Main. Sin ella, cualquier
            // viewModelScope.launch falla con "Main dispatcher not initialized"
            @get:Rule
            val mainDispatcherRule = MainDispatcherRule()

            private val repository = FakeItemRepository()

            // Los casos de uso son los REALES; lo único falso es el repositorio.
            // Así el test comprueba la orquestación de verdad.
            private fun viewModel() = MainViewModel(
                getItemsUseCase = GetItemsUseCase(repository),
                refreshItemsUseCase = RefreshItemsUseCase(repository)
            )

            // runTest ejecuta corrutinas en un tiempo simulado: nada de esperas reales
            @Test
            fun `exposes the items published by the repository`() = runTest {
                repository.items.value = listOf(ITEM)

                // .test { } es Turbine: colecciona el flujo y permite ir consumiendo
                // sus emisiones una a una. Recuerda que con WhileSubscribed el flujo no
                // arranca hasta que alguien lo colecciona: sin esto no habría estado.
                viewModel().uiState.test {
                    // expectMostRecentItem = "dame el último estado", ignorando intermedios
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

            /** Comprueba el camino datos -> UI: si cambian los datos, la pantalla cambia. */
            @Test
            fun `reacts to new data without any user action`() = runTest {
                viewModel().uiState.test {
                    assertEquals(MainUiState.Success(emptyList()), expectMostRecentItem())

                    // Nadie pulsa nada: solo cambian los datos guardados
                    repository.items.value = listOf(ITEM)

                    // awaitItem = "exijo que llegue una emisión nueva"
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

        /**
         * Tests del caso de uso del detalle. Aquí no hace falta MainDispatcherRule:
         * un caso de uso no tiene viewModelScope, es Kotlin puro.
         */
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

        // Paletas de color para modo claro y oscuro, construidas con los colores de Color.kt
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

        /**
         * TEMA de la app: envuelve toda la interfaz y le da colores y tipografías.
         * Cualquier composable de dentro puede consultarlos con MaterialTheme.
         *
         * @param darkTheme por defecto sigue el ajuste del sistema (modo oscuro).
         * @param dynamicColor en Android 12+ toma los colores del fondo de pantalla
         *        del usuario (Material You). Ponlo a false si tu app tiene colores
         *        de marca que deben respetarse siempre.
         */
        @Composable
        fun AppTheme(
            darkTheme: Boolean = isSystemInDarkTheme(),
            dynamicColor: Boolean = true,
            content: @Composable () -> Unit
        ) {
            val colorScheme = when {
                // VERSION_CODES.S = Android 12, la versión que trajo Material You
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
          - **Repository**: contract and implementation, single source of truth
            coordinating local and remote sources.
        - **DI**: Dagger Hilt for dependency injection.

        ## Package Structure
        `$packageName`
        - `data/`: Repository (contract + implementation), DAOs, Database, Entities,
          API Service, DTOs
        - `di/`: Hilt modules (`DatabaseModule`, `NetworkModule`, `RepositoryModule`,
          `DispatcherModule`)
        - `domain/`: Business models and UseCases
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
