# Android Studio Template Plugin (Hilt + Retrofit + Room)

Este proyecto es un **Plugin de IntelliJ IDEA / Android Studio** que añade una plantilla personalizada al asistente de creación de proyectos ("New Project Wizard").

> 📖 Si quieres entender cómo está construido el plugin o crear uno desde cero, lee la
> **[Guía de creación del plugin](GUIA-CREACION-PLUGIN.md)**: requisitos, lenguaje,
> arquitectura, APIs usadas y los errores típicos con sus soluciones.

## 🚀 Características de la plantilla generada

Cuando se crea un proyecto usando este plugin, se genera una arquitectura limpia (**Clean Architecture**) con:

1. **Inyección de dependencias con Dagger Hilt 2.58**
   - Configurado con KSP (`com.google.devtools.ksp` 2.1.21-2.0.2).
   - Clase `Application` anotada con `@HiltAndroidApp` y registrada en `AndroidManifest.xml`.
   - `MainActivity` anotada con `@AndroidEntryPoint`.
   - Módulos Hilt para Base de datos (`DatabaseModule`) y Red (`NetworkModule`).

2. **Red con Retrofit 3.0.0 + kotlinx.serialization**
   - OkHttp Logging Interceptor 4.12.0.
   - `kotlinx.serialization` 1.8.1 para mapeo seguro de DTOs JSON.
   - Interfaz `ApiService` con métodos `suspend`.
   - Permiso `INTERNET` añadido al manifest.

3. **Persistencia local con Room 2.8.4**
   - Plugin oficial `androidx.room` con exportación de esquemas (`schemas/`).
   - Entidad `@Entity`, DAO `@Dao` con `Flow`, e interfaz `@Database`.

4. **Interfaz de usuario reactiva con Jetpack Compose & Material3**
   - Compose BoM `2025.06.00` con `collectAsStateWithLifecycle()`.
   - `ViewModel` con `@HiltViewModel`.
   - `UiState` inmutable (Loading, Success, Error).
   - Tema Material3 personalizable (`Theme.kt`, `Color.kt`, `Type.kt`).

5. **Navegación type-safe con Navigation Compose 2.9.1**
   - Rutas como clases `@Serializable` (`ui/navigation/AppDestinations.kt`), sin strings.
   - Grafo centralizado en `AppNavHost`; las pantallas reciben lambdas, nunca el `NavController`.
   - Ejemplo lista → detalle, con los argumentos leídos en el ViewModel vía
     `savedStateHandle.toRoute<ItemDetailRoute>()`.

6. **Tests unitarios listos para ejecutar (`./gradlew testDebugUnitTest`)**
   - JUnit 4.13.2, `kotlinx-coroutines-test` 1.8.1 y Turbine 1.1.0 (versiones alineadas
     con las corrutinas que resuelve el proyecto, guías 41 y 42).
   - `MainDispatcherRule` sustituyendo `Dispatchers.Main` y `FakeItemRepository` como
     único fake: los casos de uso bajo prueba son los reales.
   - 7 tests que cubren estado, reactividad datos→UI y fallo de refresco.

7. **Calidad de producción**
   - Dispatchers inyectados con qualifier `@IoDispatcher` (sustituibles en tests).
   - `flowOn` en la capa de datos y `CancellationException` propagada, no capturada.
   - Composables divididos en *stateful* / *stateless* con `@Preview`.
   - R8 activado en `release` (`isMinifyEnabled` + `isShrinkResources`) con reglas para
     Retrofit, Room y `kotlinx.serialization`.

8. **Catálogo de versiones (`gradle/libs.versions.toml`)**
   - Todas las versiones alineadas según las guías del proyecto `AprenderDesarrolloAndroid`.

9. **Parámetros del asistente**
   - Nombre del fichero de base de datos Room y URL base de Retrofit configurables al
     crear el proyecto.

---

## 🛠️ Cómo compilar e instalar el Plugin en Android Studio

> ⚠️ **Importante:** la API `wizard-template` de Android Studio **no es estable entre
> versiones**, por lo que el plugin debe compilarse contra la **misma versión** de
> Android Studio en la que se va a instalar. El `build.gradle.kts` usa por defecto la
> instalación local de JetBrains Toolbox
> (`~/.local/share/JetBrains/Toolbox/apps/android-studio`). Si tu instalación está en
> otra ruta: `./gradlew buildPlugin -PandroidStudioPath=/ruta/a/android-studio`.

### 1. Compilar el Plugin
Ejecuta el siguiente comando dentro de esta carpeta (necesita JDK 21; puedes usar el
JBR que trae Android Studio):

```bash
JAVA_HOME=~/.local/share/JetBrains/Toolbox/apps/android-studio/jbr ./gradlew buildPlugin
```

El archivo generado estará disponible en:
`build/distributions/AndroidStudioTemplate-1.0.2.zip`

> 📦 En `dist/AndroidStudioTemplate-1.0.2.zip` hay una copia ya compilada, lista para
> descargar desde GitHub. Está construida contra Android Studio Quail 2026.1.1
> (`AI-261.*`): si usas otra versión mayor del IDE, compila el plugin tú mismo con el
> comando de arriba.

### 2. Instalar en Android Studio
1. Abre **Android Studio**.
2. Ve a **Settings** -> **Plugins**.
3. Haz clic en el icono del engranaje ⚙️ (esquina superior derecha) y selecciona **Install Plugin from Disk...**.
4. Selecciona el archivo Zip (`dist/AndroidStudioTemplate-1.0.2.zip`, o el que hayas
   compilado en `build/distributions/`).
5. Reinicia Android Studio.

### 3. Crear un nuevo proyecto
1. Ve a **File** -> **New** -> **New Project...**.
2. En la categoría **Phone and Tablet** de la galería de plantillas verás **Android (Hilt + Retrofit + Room)** (al final de la lista).
3. Especifica el Name, Package name y Minimum SDK en la página estándar del asistente.
4. ¡El proyecto se generará listo para compilar!

---

## ⚠️ Nota sobre `install_templates.sh` y la carpeta `templates/`

El script `install_templates.sh` y las plantillas FreeMarker (`templates/projects/*.ftl`)
usan el mecanismo antiguo de plantillas de Android Studio, **eliminado en Android
Studio 4.1 (2020)**. Android Studio ya no lee la carpeta
`~/.config|.local/share/Google/AndroidStudio*/templates/`, así que ese método **no
funciona** en versiones modernas. La única vía soportada es el plugin
(`wizardTemplateProvider`), descrito arriba.
