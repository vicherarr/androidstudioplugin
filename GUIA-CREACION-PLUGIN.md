# Guía: cómo crear un plugin de plantillas para Android Studio desde cero

Este documento explica cómo está construido este plugin y todo lo que necesitas saber
para crear uno similar partiendo de cero: requisitos, lenguaje, estructura, APIs,
proceso de compilación, instalación y los errores típicos (todos los de este documento
nos pasaron de verdad durante el desarrollo).

### Cómo leer esta guía

La guía está dividida en dos partes:

- **Parte A — Para principiantes.** Empieza aquí si nunca has programado. Explica los
  conceptos básicos, qué herramientas instalar y cómo, y unos pasos numerados y
  secuenciales para construir e instalar el plugin sin dar nada por sabido.
- **Parte B — Referencia técnica.** El detalle fino de cada fichero, cada API y cada
  error. Léela cuando ya tengas lo básico funcionando o si quieres entender el *por qué*
  de cada decisión.

No hace falta leerlo todo de golpe. Si solo quieres **usar** el plugin, con la Parte A
te sobra.

---

---

# PARTE A — Para quien nunca ha programado

## A.0 Un vistazo general: ¿qué vas a hacer exactamente?

Antes de instalar nada, ten clara la idea de fondo, porque es más simple de lo que
parece. Crear este plugin es como **cocinar con una receta**:

1. Escribes unos **ficheros de texto** (el "código" y la "configuración"). Son texto
   normal, como un documento de Word pero sin formato.
2. Ejecutas **una sola orden** en la terminal. Una herramienta (Gradle) lee esos
   ficheros y los "cocina": los convierte en un archivo **`.zip`**. A ese paso se le
   llama **compilar**.
3. Ese `.zip` es el plugin terminado. Lo **instalas** dentro de Android Studio (como
   quien instala una extensión en el navegador) y **reinicias**.
4. A partir de ahí, al crear un proyecto nuevo aparece tu plantilla.

Todo lo demás de esta guía son detalles de esos cuatro pasos. No necesitas entender
todo el código para que funcione: necesitas los ficheros correctos en su sitio y
ejecutar la orden de compilar.

## A.1 Las herramientas que necesitas instalar

Son pocas. Esta tabla resume qué instalar y para qué; el "cómo" viene justo después.

| Herramienta | ¿Para qué sirve? | ¿Obligatoria? |
|---|---|---|
| **Android Studio** | Es el programa al que le añades el plugin y, además, trae dentro un JDK que puedes reutilizar para compilar | **Sí** |
| **JDK 21** (Java) | Compilar el plugin. **Puedes usar el que ya viene dentro de Android Studio**, así te ahorras instalarlo aparte | **Sí** (pero suele venir con Android Studio) |
| **Gradle** | Compilar el proyecto | **No hace falta instalarlo**: el `gradlew` del proyecto lo descarga solo |
| **La terminal** | Escribir la orden de compilar | **Sí**, pero ya viene con tu sistema operativo |
| **Git** | Subir el código a GitHub | **No**, solo si quieres compartirlo online |

Fíjate en lo importante: de "instalaciones" reales, en la práctica **solo instalas
Android Studio**. El JDK viene dentro y Gradle se descarga solo.

## A.2 Instalación paso a paso

### Paso 1 — Instalar Android Studio

1. Entra en la página oficial: **https://developer.android.com/studio**
2. Pulsa el botón de descarga. La web detecta tu sistema (Windows, macOS o Linux).
3. Instálalo:
   - **Windows:** ejecuta el `.exe` descargado y sigue el asistente (Siguiente →
     Siguiente → Instalar).
   - **macOS:** abre el `.dmg` y arrastra Android Studio a la carpeta *Aplicaciones*.
   - **Linux:** descomprime el `.tar.gz` en una carpeta (por ejemplo tu carpeta
     personal) y ejecuta `bin/studio.sh`. Alternativa recomendada en Linux: instalar
     **JetBrains Toolbox** y desde ahí instalar Android Studio (así se actualiza solo).
4. Ábrelo una vez y deja que termine su configuración inicial (descarga componentes de
   Android; puede tardar). Con esto ya tienes el IDE **y** un JDK dentro de él.

> 💡 Android Studio incluye su propio Java, llamado **JBR** (JetBrains Runtime), en una
> subcarpeta `jbr` dentro de donde lo instalaste. Ese JBR sirve perfectamente como
> JDK 21 para compilar el plugin, así no tienes que instalar Java por separado.

### Paso 2 — Localizar el JDK (el Java de Android Studio)

Necesitas saber **la ruta** de ese JBR, porque se la darás a la orden de compilar.
Según dónde instalaste Android Studio, la carpeta `jbr` suele estar en:

- **Windows:** `C:\Program Files\Android\Android Studio\jbr`
- **macOS:** `/Applications/Android Studio.app/Contents/jbr`
- **Linux (Toolbox):** `~/.local/share/JetBrains/Toolbox/apps/android-studio/jbr`
- **Linux (manual):** dentro de la carpeta donde descomprimiste, subcarpeta `jbr`

Guarda esa ruta, la usarás en el Paso 5. (Si prefieres no complicarte con rutas, en el
Paso 5 se explica la alternativa de instalar un JDK 21 "normal".)

### Paso 3 — (Opcional) Instalar Git

Solo si vas a subir el proyecto a GitHub:

- **Windows:** descarga e instala desde **https://git-scm.com/download/win**
- **macOS:** abre la terminal y escribe `git --version`; si no lo tienes, el sistema te
  ofrecerá instalarlo. O instálalo con Homebrew: `brew install git`.
- **Linux:** con el gestor de paquetes de tu distribución, p. ej. `sudo pacman -S git`
  (Arch/CachyOS) o `sudo apt install git` (Ubuntu/Debian).

### Paso 4 — Saber abrir la terminal

La "terminal" es la ventana donde escribirás la orden de compilar:

- **Windows:** pulsa la tecla Windows, escribe `PowerShell` o `Terminal` y ábrelo.
- **macOS:** pulsa `Cmd + Espacio`, escribe `Terminal` y pulsa Enter.
- **Linux:** normalmente `Ctrl + Alt + T`, o busca "Terminal" en tus aplicaciones.

Dentro de la terminal, dos órdenes que usarás constantemente:

- `cd <carpeta>` → "entrar en" una carpeta (change directory). Ej: `cd Descargas`.
- `ls` (Linux/Mac) o `dir` (Windows) → listar lo que hay en la carpeta actual.

## A.3 Comprobar que todo está listo (antes de compilar)

Abre la terminal y comprueba que el Java de Android Studio funciona. Sustituye la ruta
por la tuya del Paso 2. Ejemplo en Linux con Toolbox:

```bash
~/.local/share/JetBrains/Toolbox/apps/android-studio/jbr/bin/java -version
```

En Windows (PowerShell) sería algo como:

```powershell
& "C:\Program Files\Android\Android Studio\jbr\bin\java.exe" -version
```

Si ves un texto tipo `openjdk version "21..."`, ¡perfecto! Ya puedes compilar. Si da
error de "no se encuentra", revisa la ruta del Paso 2.

## A.4 Construir e instalar ESTE plugin, paso a paso

Estos pasos parten de que ya tienes la carpeta del proyecto (este mismo repositorio).
Si lo bajaste de GitHub como `.zip`, descomprímelo primero.

1. **Abre la terminal** (Paso 4 de arriba).

2. **Entra en la carpeta del proyecto** con `cd`. Por ejemplo, si está en tu carpeta
   personal:
   ```bash
   cd ~/develop/AndroidStudioTemplate
   ```
   (En Windows sería p. ej. `cd C:\Users\TuUsuario\Downloads\AndroidStudioTemplate`.)
   Para confirmar que estás en el sitio correcto, lista los ficheros (`ls` o `dir`):
   debes ver `build.gradle.kts`, `gradlew`, la carpeta `src`, etc.

3. **Lanza la compilación.** Esta única orden hace *todo*. En Linux/macOS, poniendo
   delante la ruta al Java de Android Studio (la del Paso 2):
   ```bash
   JAVA_HOME=~/.local/share/JetBrains/Toolbox/apps/android-studio/jbr ./gradlew buildPlugin
   ```
   En Windows (PowerShell):
   ```powershell
   $env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"; .\gradlew.bat buildPlugin
   ```
   La **primera vez tardará varios minutos**: Gradle se descarga a sí mismo y baja las
   dependencias. Es normal. Cuando termine bien, verás el mensaje **`BUILD SUCCESSFUL`**.

   > Si `JAVA_HOME` te resulta lioso: instala un **JDK 21** normal (Eclipse Temurin,
   > desde https://adoptium.net) y así podrás ejecutar solo `./gradlew buildPlugin` sin
   > la parte de `JAVA_HOME` delante.

4. **Encuentra el plugin ya compilado.** Se ha creado aquí:
   ```
   build/distributions/AndroidStudioTemplate-1.0.3.zip
   ```
   Ese `.zip` **es tu plugin**. (Debe pesar unos pocos KB; si pesa más de 1 MB, algo va
   mal — mira la Parte B, §8.1.)

5. **Instálalo en Android Studio:**
   1. Abre Android Studio.
   2. Ve a **Settings** (⚙️) → **Plugins**.
   3. Pulsa el icono del engranaje ⚙️ (arriba, junto a "Installed") → **Install Plugin
      from Disk...**.
   4. Selecciona el `.zip` del paso 4.
   5. Pulsa **OK** y luego **Restart IDE** para reiniciar.

6. **Pruébalo.** Tras reiniciar: **File → New → New Project...**. En la pestaña
   **Phone and Tablet**, baja hasta el final de la galería: ahí está
   **"Android (Hilt + Retrofit + Room)"**. Selecciónala, pon nombre y paquete, y crea
   el proyecto.

¡Eso es todo! Si algo no aparece, ve a la Parte B, §8.3 (tabla de errores comunes) y a
§8.4 (cómo mirar el registro `idea.log` para ver qué pasó).

## A.5 Si quieres crear TU PROPIO plugin desde cero

Cuando ya hayas conseguido compilar este, crear uno tuyo es repetir el patrón con tus
ficheros. El esqueleto mínimo son solo estos ficheros (los detalles de cada uno están
en la Parte B, que te indico entre paréntesis):

1. **`build.gradle.kts`** — la "receta de compilación": qué versión de Android Studio y
   qué herramientas usar. (Copia el de este proyecto y ajústalo → Parte B, §5.)
2. **`gradle.properties`** — un par de ajustes. (§5.)
3. **`settings.gradle.kts`** + la carpeta **`gradle/wrapper/`** y el fichero
   **`gradlew`** — el lanzador de Gradle. (Lo más fácil: copiarlos tal cual de este
   proyecto.)
4. **`src/main/resources/META-INF/plugin.xml`** — la "ficha de identidad" del plugin:
   su nombre, su id y qué función del IDE extiende. (§6.)
5. **`src/main/kotlin/.../TuProveedor.kt`** — el código Kotlin que define tu plantilla y
   qué ficheros genera. (§7.)

El orden recomendado para trabajar:

1. Copia este proyecto entero a una carpeta nueva y renómbralo. Así partes de algo que
   ya compila, en vez de la página en blanco.
2. Cambia el `id`, el `name` y el `vendor` en `plugin.xml`.
3. Cambia el texto de la plantilla (`name`, `description`) en el proveedor `.kt`.
4. Cambia lo que genera: edita `ProjectGenerator.kt` / `FileTemplates.kt` para que
   escriba los ficheros que tú quieras.
5. Compila y prueba con `./gradlew buildPlugin` cada vez que hagas un cambio, o usa
   `./gradlew runIde` (Parte B, §8.4) para abrir un Android Studio de pruebas con tu
   plugin ya cargado, sin tocar tu instalación real.

> ⚠️ **La regla de oro** (se explica a fondo en la Parte B, §5): un plugin de plantillas
> hay que compilarlo **contra la misma versión de Android Studio** donde lo vas a usar.
> Si actualizas Android Studio a una versión mayor, tendrás que **recompilar y volver a
> instalar** el plugin. Es la causa nº 1 de "lo instalé pero no aparece".

---

---

# PARTE B — Referencia técnica

> A partir de aquí la guía entra en detalle: la arquitectura interna, cada API, cada
> fichero y los errores con su explicación. Si vienes de la Parte A y algo no te suena,
> no pasa nada: úsala como diccionario de consulta.

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
            // Solo escribir en la pasada REAL, nunca en la de validación (ver §7.3)
            if (!javaClass.simpleName.contains("FindReferences")) {
                val rootDir = moduleData.projectTemplateData.rootDir
                ProjectGenerator.generate(
                    targetDir = rootDir,
                    appName = moduleData.themesData.appName,
                    packageName = moduleData.packageName
                )
                // Refrescar el VFS para que el IDE vea lo escrito con java.io
                LocalFileSystem.getInstance().findFileByIoFile(rootDir)?.let {
                    VfsUtil.markDirtyAndRefresh(true, true, true, it)
                }
            }
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

Es la aproximación más simple posible, pero escribir con `java.io.File` tiene **dos
trampas serias** que descubrimos a base de golpes:

1. **El recipe se ejecuta DOS veces.** El asistente hace primero una pasada de
   *validación* ("dry run") con un executor especial (`FindReferencesRecipeExecutor`)
   que se supone que no toca el disco — los métodos del `RecipeExecutor` (`save()`,
   etc.) no hacen nada en esa pasada, pero `java.io.File` escribe igual. Resultado:
   los ficheros aparecen en disco *antes de tiempo*, fuera del VFS (el sistema de
   ficheros virtual del IDE). Cuando después Android Studio genera su propio scaffold,
   ve que `build.gradle.kts` "ya existe" pero no está en el VFS, y su generador de
   módulos aborta con `IllegalStateException: Build model for root project not found`.
   El síntoma: el proyecto se crea, los ficheros están en *Project Files*, pero la
   vista *Project* sale vacía y nunca se lanza la sincronización de Gradle. Por eso el
   recipe comprueba el tipo del executor y **solo escribe en la pasada real**.

2. **El IDE no ve lo que escribes.** El VFS trabaja con una caché; los ficheros
   escritos con `java.io` no existen para el IDE hasta que se refresca. De ahí el
   `VfsUtil.markDirtyAndRefresh(...)` al final del recipe.

La alternativa "canónica" (la que usan las plantillas integradas de Android Studio) es
hacerlo todo con los métodos del `RecipeExecutor`: `save()` para ficheros nuevos,
`mergeXml()` para fusionar manifests/recursos, `addPlugin()`/`addDependency()` para
tocar los build files. Esos métodos son inmunes a los dos problemas de arriba (no
hacen nada en el dry run y escriben vía VFS), pero tienen una limitación: `save()`
**nunca sobreescribe** un fichero existente, así que no sirven si tu plantilla quiere
reemplazar por completo los build files que genera el scaffold estándar, como hace
esta. Elige según tu caso.

---

## 8. Compilar, instalar y depurar

### 8.1 Compilar

```bash
JAVA_HOME=~/.local/share/JetBrains/Toolbox/apps/android-studio/jbr ./gradlew buildPlugin
```

(El `JAVA_HOME` puede ser cualquier JDK 21; el JBR del propio Android Studio es el más
a mano.) Resultado: `build/distributions/AndroidStudioTemplate-1.0.3.zip`.

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
| El proyecto se crea pero la vista *Project* sale vacía (solo un `scratch.kts`), no hay sync de Gradle; en `idea.log`: `IllegalStateException: Build model for root project not found` | El recipe escribió con `java.io.File` durante la pasada de validación (dry run) del asistente, dejando los build files fuera del VFS y rompiendo el render de Android Studio | Escribir solo en la pasada real (comprobar que el executor no es `FindReferencesRecipeExecutor`) y refrescar el VFS al terminar (§7.3) |
| El proyecto generado falla al sincronizar: `Plugin [id: '...'] was not found in any of the following sources` | Id de plugin mal escrito en el `libs.versions.toml` de la plantilla (p. ej. `com.android.kotlin.android` en vez de `org.jetbrains.kotlin.android`), o el wrapper que pone Android Studio (Gradle 9.x) es incompatible con el AGP de la plantilla | Corregir el catálogo y **sobreescribir `gradle/wrapper/gradle-wrapper.properties`** para fijar una versión de Gradle compatible con tu AGP. Valida siempre la plantilla compilando un proyecto generado por terminal: `./gradlew :app:assembleDebug` |
| El proyecto generado compila a medias: falta `android.useAndroidX=true`, o falla `processDebugResources` con `Theme.MaterialComponents... not found` | La plantilla convive con el scaffold estándar de Android Studio, que escribe un `gradle.properties` mínimo (sin AndroidX) y un `values-night/themes.xml` que referencia Material Components | Sobreescribir desde la plantilla los ficheros del scaffold que entren en conflicto: `gradle.properties`, `values-night/themes.xml`, wrapper... (lo que el scaffold deja y tu plantilla no cubre sigue ahí) |

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
