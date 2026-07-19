# Guía: cómo crear un plugin de plantillas para Android Studio desde cero

Este documento explica cómo está construido este plugin y todo lo que necesitas saber
para crear uno similar partiendo de cero: requisitos, lenguaje, estructura, APIs,
proceso de compilación, instalación y los errores típicos (todos los de este documento
nos pasaron de verdad durante el desarrollo).

---

## 1. ¿Qué es exactamente este plugin?

Android Studio está construido sobre la **IntelliJ Platform** (la misma base que
IntelliJ IDEA). Un "plugin de Android Studio" es, por tanto, un **plugin de IntelliJ
Platform**: un `.zip` que contiene un `.jar` con clases compiladas para la JVM y un
descriptor XML (`plugin.xml`).

Lo que hace este plugin en concreto es **añadir una plantilla a la galería del
asistente de New Project** (categoría *Phone and Tablet*). Al seleccionarla, genera un
proyecto Android completo con Hilt, Retrofit, Room, Jetpack Compose y Clean
Architecture.

---

## 2. Requisitos

| Requisito | Valor usado aquí | Notas |
|---|---|---|
| Lenguaje | **Kotlin** (JVM) | Java también vale, pero la API de plantillas es un DSL Kotlin |
| JDK | **21** | Obligatorio desde la plataforma 2024.2; Android Studio trae uno en `<instalación>/jbr` |
| Build system | **Gradle** (wrapper 8.13) | Único sistema soportado oficialmente |
| Plugin de Gradle | **`org.jetbrains.intellij.platform` 2.x** | La versión 1.x (`org.jetbrains.intellij`) está obsoleta y no maneja bien las plataformas modernas |
| Kotlin Gradle Plugin | **2.3.0** | Debe poder leer los metadatos Kotlin de los jars del IDE (ver §8.3) |
| IDE objetivo | Android Studio **instalado localmente** | Se compila contra esa instalación exacta (ver §5, es el punto más importante) |

No hace falta tener IntelliJ IDEA ni el SDK de Android: el propio Android Studio
instalado hace de "SDK" contra el que se compila.

### El lenguaje

Todo el plugin es **Kotlin puro para JVM** (no Android). Dos motivos:

1. La API de plantillas de Android Studio (`com.android.tools.idea.wizard.template`)
   es un **DSL de Kotlin** (`template { ... }`, lambdas, propiedades con `var`).
   Usarla desde Java sería muy incómodo.
2. La IntelliJ Platform moderna está escrita mayoritariamente en Kotlin.

Los ficheros que genera la plantilla (el "contenido" del proyecto resultante) son
simples **strings de Kotlin** con `trimIndent()` en `FileTemplates.kt` — no hay motor
de plantillas (FreeMarker, Velocity...) involucrado.

---

## 3. Estructura del proyecto

```
AndroidStudioTemplate/
├── build.gradle.kts                  ← configuración clave (ver §5)
├── settings.gradle.kts
├── gradle.properties                 ← desactiva el empaquetado de kotlin-stdlib
├── gradlew / gradle/wrapper/         ← wrapper de Gradle 8.13
└── src/main/
    ├── resources/META-INF/plugin.xml ← descriptor del plugin (ver §6)
    └── kotlin/com/aprender/template/
        ├── wizard/
        │   └── AndroidStudioWizardTemplateProvider.kt  ← registra la plantilla (ver §7)
        └── generator/
            ├── ProjectGenerator.kt   ← escribe el árbol de ficheros del proyecto
            └── FileTemplates.kt      ← contenido de cada fichero como strings Kotlin
```

Son solo **3 clases Kotlin**. Todo lo demás es configuración.

---

## 4. La pieza central: el extension point `wizardTemplateProvider`

Los plugins de IntelliJ extienden el IDE a través de **extension points** (puntos de
extensión) declarados en `plugin.xml`. La pregunta crítica es: *¿cuál hace que algo
aparezca en el New Project de Android Studio?*

**Solo hay uno válido**, declarado por el plugin Android del IDE:

```
com.android.tools.idea.wizard.template.wizardTemplateProvider
```

⚠️ **Trampa importante**: la IntelliJ Platform tiene extension points genéricos para
asistentes de proyecto (`com.intellij.moduleBuilder`, `newProjectWizard`,
`projectTemplatesFactory`, `directoryProjectGenerator`). **Ninguno aparece en Android
Studio**, porque Android Studio reemplaza el asistente genérico de IntelliJ por el
suyo propio (la galería de Phone and Tablet / Wear / TV...). Ese asistente **solo** se
alimenta de `wizardTemplateProvider`. Este proyecto originalmente registraba los
cuatro genéricos "por si acaso": eran código muerto y se eliminaron.

Tampoco funciona el mecanismo antiguo de copiar plantillas FreeMarker (`.ftl` +
`template.xml`) a `~/.config/Google/AndroidStudio*/templates/`: **Google lo eliminó en
Android Studio 4.1 (2020)**. Cualquier tutorial que lo mencione está obsoleto.

---

## 5. `build.gradle.kts`: compilar contra el IDE instalado

Esta es la parte donde este plugin estaba roto y la lección más importante de toda la
guía:

> **La API `wizard-template` NO es estable entre versiones de Android Studio.**
> Sus clases (`Template`, `ProjectTemplateData`, `ModuleTemplateData`...) son data
> classes de Kotlin cuyos constructores y propiedades cambian en cada versión mayor.
> Un plugin compilado contra Android Studio 2024.1 **se instala** en 2026.1 (si el
> rango `since-build`/`until-build` lo permite) pero al intentar cargar la plantilla
> lanza `NoSuchMethodError` internamente y el IDE **la descarta en silencio**: el
> plugin figura como instalado y la opción simplemente no aparece. Sin errores visibles.

La solución es compilar contra la **misma instalación** de Android Studio donde se va
a usar, con el IntelliJ Platform Gradle Plugin 2.x y su función `local()`:

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.7.0"
}

repositories {
    mavenCentral()
    intellijPlatform { defaultRepositories() }   // repos de JetBrains para dependencias del plugin
}

// Ruta a la instalación local (aquí, la de JetBrains Toolbox)
val androidStudioPath = providers.gradleProperty("androidStudioPath")
    .orElse(System.getProperty("user.home") + "/.local/share/JetBrains/Toolbox/apps/android-studio")

dependencies {
    intellijPlatform {
        local(androidStudioPath.get())        // usa el IDE instalado como SDK
        bundledPlugin("org.jetbrains.android") // añade el plugin Android (y wizard-template.jar) al classpath
    }
    compileOnly(kotlin("stdlib"))             // solo para compilar; en runtime la aporta el IDE
}

intellijPlatform {
    buildSearchableOptions = false            // evita arrancar el IDE headless en cada build
    instrumentCode = false
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "261"                // build de la versión contra la que compilas
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
}
```

Y en `gradle.properties`:

```properties
# El IDE ya aporta la kotlin-stdlib en runtime; no empaquetarla en el plugin
kotlin.stdlib.default.dependency=false
```

Claves de este bloque:

- **`local(...)`** apunta a la carpeta de instalación del IDE. Alternativa si no
  quieres depender de la instalación local: `androidStudio("2026.1.1.x")`, que
  descarga esa versión (~1,5 GB) de los repos de JetBrains.
- **`bundledPlugin("org.jetbrains.android")`** es imprescindible: la API de
  plantillas vive en `plugins/android/lib/wizard-template.jar` dentro del IDE, no en
  la plataforma base.
- **`sinceBuild`**: el número de build del IDE. Se consulta en
  `<instalación>/product-info.json` o `build.txt` (para Quail 2026.1.1 es
  `AI-261.23567.138` → `sinceBuild = "261"`). No pongas un rango amplio hacia atrás:
  compilaste contra una ABI concreta.
- **¿Cómo saber el build de tu versión?** `Help → About` en el IDE, o
  `cat <instalación>/build.txt`.

---

## 6. `plugin.xml`: el descriptor

```xml
<idea-plugin>
    <id>com.aprender.template</id>                <!-- único y permanente -->
    <name>Android Studio Template (Hilt + Retrofit + Room)</name>
    <vendor email="developer@aprenderandroid.com">AprenderDesarrolloAndroid</vendor>
    <description><![CDATA[ ... HTML ... ]]></description>

    <!-- Dependencias de otros plugins: sin esta línea el classloader
         del plugin NO ve las clases de com.android.tools.* -->
    <depends>com.intellij.modules.platform</depends>
    <depends>org.jetbrains.android</depends>

    <!-- El único registro necesario -->
    <extensions defaultExtensionNs="com.android.tools.idea.wizard.template">
        <wizardTemplateProvider
            implementation="com.aprender.template.wizard.AndroidStudioWizardTemplateProvider"/>
    </extensions>
</idea-plugin>
```

Notas:

- `<depends>org.jetbrains.android</depends>` cumple dos funciones: hace que el plugin
  solo se active donde exista el plugin Android, y **da acceso en runtime a sus
  clases** (los plugins de IntelliJ tienen classloaders aislados).
- No escribas `<idea-version since-build="...">` a mano: la tarea `patchPluginXml`
  del plugin de Gradle lo inyecta desde `pluginConfiguration.ideaVersion`.

---

## 7. El código: proveedor, DSL de plantilla y recipe

### 7.1 El proveedor

```kotlin
class AndroidStudioWizardTemplateProvider : WizardTemplateProvider() {
    override fun getTemplates(): List<Template> = listOf(androidHiltRetrofitRoomTemplate)
}
```

Android Studio instancia esta clase al abrir el asistente y añade cada `Template`
devuelto a la galería.

### 7.2 La plantilla (DSL)

```kotlin
val androidHiltRetrofitRoomTemplate: Template
    get() = template {
        name = "Android (Hilt + Retrofit + Room)"       // texto en la galería
        description = "Creates a clean architecture..." // tooltip/descripción
        minApi = 26
        category = Category.Application                 // categoría Application = proyecto completo
        formFactor = FormFactor.Mobile                  // pestaña "Phone and Tablet"
        screens = listOf(WizardUiContext.NewProject)    // dónde aparece: solo New Project

        recipe = { data: TemplateData ->
            val moduleData = data as ModuleTemplateData
            ProjectGenerator.generate(
                targetDir = moduleData.projectTemplateData.rootDir,
                appName = moduleData.themesData.appName,
                packageName = moduleData.packageName
            )
        }
    }
```

Conceptos:

- **`category` + `formFactor`** determinan la pestaña de la galería.
  `Category.Application` + `FormFactor.Mobile` → *Phone and Tablet*.
- **`screens`** determina el contexto: `NewProject` (galería de proyecto nuevo),
  `NewModule` (File → New → New Module), `ActivityGallery`, `MenuEntry`...
  Aquí solo `NewProject`, porque el recipe escribe el proyecto entero y en un
  proyecto existente (`NewModule`) machacaría ficheros.
- **`recipe`** es la lambda que se ejecuta al pulsar *Finish*. Recibe un
  `RecipeExecutor` (como receiver) y un `TemplateData`.
- **No dupliques campos**: el asistente estándar ya pide *Name*, *Package name*,
  *Save location* y *Minimum SDK* para toda plantilla `Application`. Esos valores
  llegan en `ModuleTemplateData`:
  - `moduleData.packageName` → paquete elegido por el usuario
  - `moduleData.projectTemplateData.rootDir` → carpeta raíz del proyecto
  - `moduleData.themesData.appName` → nombre de la app
  - `moduleData.apis` → datos de minSdk/targetSdk si los necesitas
  
  (La versión inicial de este plugin añadía sus propios `stringParameter` +
  `TextFieldWidget` para nombre y paquete: campos duplicados y confusos.)

### 7.3 El generador

`ProjectGenerator` recorre una lista de rutas y escribe cada fichero con
`java.io.File.writeText()`. `FileTemplates` contiene el contenido de cada fichero
(build.gradle.kts, manifest, clases Kotlin del proyecto generado...) como funciones
que devuelven strings, interpolando `packageName` y `appName`.

Es la aproximación más simple posible. La alternativa "canónica" es usar los métodos
del `RecipeExecutor` (`save()`, `createDirectory()`, `addDependency()`...), que se
integran mejor con el sistema de ficheros virtual del IDE, pero escribir con
`java.io.File` funciona porque el IDE refresca e importa el proyecto Gradle al
terminar el asistente.

---

## 8. Compilar, instalar y depurar

### 8.1 Compilar

```bash
JAVA_HOME=~/.local/share/JetBrains/Toolbox/apps/android-studio/jbr ./gradlew buildPlugin
```

(El `JAVA_HOME` puede ser cualquier JDK 21; el JBR del propio Android Studio es el más
a mano.) Resultado: `build/distributions/AndroidStudioTemplate-1.0.0.zip`.

El zip debe contener **solo tu jar** (aquí ~16 KB). Si ves dentro
`kotlin-stdlib-*.jar`, olvidaste `kotlin.stdlib.default.dependency=false` y estás
empaquetando una stdlib que chocará con la del IDE.

### 8.2 Instalar

Opción A (interfaz): **Settings → Plugins → ⚙️ → Install Plugin from Disk...** → zip
→ reiniciar.

Opción B (manual): descomprimir el zip en la carpeta de plugins de usuario y
reiniciar. En Linux: `~/.local/share/Google/AndroidStudio<versión>/`.

### 8.3 Errores reales que encontramos (y sus soluciones)

| Síntoma | Causa | Solución |
|---|---|---|
| El plugin se instala pero la plantilla no aparece, sin ningún error | Compilado contra otra versión del IDE; la ABI de `wizard-template` cambió y el IDE descarta la plantilla en silencio | Recompilar contra la instalación local (§5). **Hay que repetirlo con cada actualización mayor del IDE** |
| La plantilla copiada a `~/.config/Google/.../templates/` no aparece | Mecanismo FreeMarker eliminado en AS 4.1 | Usar `wizardTemplateProvider`; no hay alternativa |
| `Unresolved reference 'trimIndent'` al compilar | `kotlin.stdlib.default.dependency=false` sin stdlib alternativa en el classpath | Añadir `compileOnly(kotlin("stdlib"))` |
| `Module was compiled with an incompatible version of Kotlin. The binary version of its metadata is 2.3.0/2.4.0...` | El compilador Kotlin del proyecto es más antiguo que el usado para compilar el IDE (solo puede leer metadatos hasta N+1) | Subir el Kotlin Gradle Plugin (aquí: 2.3.0 para leer metadatos 2.4 de Quail) |
| Registrar `moduleBuilder` / `newProjectWizard` y no ver nada | Android Studio no muestra el asistente genérico de IntelliJ | Eliminarlos; solo vale `wizardTemplateProvider` |

### 8.4 Depurar

- **`idea.log`** es tu mejor amigo: en Linux,
  `~/.cache/Google/AndroidStudio<versión>/log/idea.log`. Busca tu plugin en la línea
  `Loaded custom plugins:` — si no está ahí, ni siquiera se cargó. Las excepciones al
  construir la plantilla también acaban aquí.
- **`./gradlew runIde`** arranca una instancia sandbox del IDE con tu plugin ya
  instalado: el ciclo de desarrollo normal (no toca tu instalación real).
- **`./gradlew verifyPlugin`** valida el descriptor y compatibilidades.

---

## 9. Receta resumida para empezar desde cero

1. Crea un proyecto Gradle con el `build.gradle.kts` del §5 (ajusta `sinceBuild` y la
   ruta del IDE) y `gradle.properties` con `kotlin.stdlib.default.dependency=false`.
2. Escribe `src/main/resources/META-INF/plugin.xml` con tu `id`, `<depends
   >org.jetbrains.android</depends>` y el `wizardTemplateProvider`.
3. Crea una clase que extienda `WizardTemplateProvider` y devuelva una plantilla
   construida con el DSL `template { ... }` (§7.2).
4. En el `recipe`, lee `packageName`/`rootDir` de `ModuleTemplateData` y escribe los
   ficheros de tu proyecto.
5. `./gradlew runIde` para probar; `buildPlugin` + *Install from Disk* para instalar.
6. Cuando Android Studio se actualice de versión mayor: recompila y reinstala.

## 10. Referencias

- IntelliJ Platform Plugin SDK: https://plugins.jetbrains.com/docs/intellij/
- IntelliJ Platform Gradle Plugin 2.x: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
- Android Studio Plugin Development: https://plugins.jetbrains.com/docs/intellij/android-studio.html
- Fuente de la API de plantillas (AOSP): https://cs.android.com/android-studio (módulo `wizard/template`)
