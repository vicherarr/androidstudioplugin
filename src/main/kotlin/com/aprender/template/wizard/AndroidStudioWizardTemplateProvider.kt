package com.aprender.template.wizard

import com.android.tools.idea.wizard.template.Category
import com.android.tools.idea.wizard.template.FormFactor
import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.Template
import com.android.tools.idea.wizard.template.TemplateData
import com.android.tools.idea.wizard.template.WizardTemplateProvider
import com.android.tools.idea.wizard.template.WizardUiContext
import com.android.tools.idea.wizard.template.template
import com.aprender.template.generator.ProjectGenerator
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil

class AndroidStudioWizardTemplateProvider : WizardTemplateProvider() {
    override fun getTemplates(): List<Template> {
        return listOf(androidHiltRetrofitRoomTemplate)
    }
}

val androidHiltRetrofitRoomTemplate: Template
    get() = template {
        name = "Android (Hilt + Retrofit + Room)"
        description = "Creates a clean architecture Android project pre-configured with Hilt, Retrofit, Room, and Jetpack Compose."
        minApi = 26
        category = Category.Application
        formFactor = FormFactor.Mobile
        screens = listOf(WizardUiContext.NewProject)

        recipe = { data: TemplateData ->
            val moduleData = data as ModuleTemplateData

            // El asistente ejecuta el recipe dos veces: una pasada de validación con
            // FindReferencesRecipeExecutor (que NO debe tocar el disco) y la pasada real.
            // Escribir ficheros en la pasada de validación deja el build.gradle.kts raíz
            // fuera del VFS y el render de Android Studio aborta con "Build model for
            // root project not found", dejando el proyecto sin vincular a Gradle.
            if (!javaClass.simpleName.contains("FindReferences")) {
                val rootDir = moduleData.projectTemplateData.rootDir

                // El asistente estándar ya pide Name / Package name / Minimum SDK;
                // se reutilizan esos valores en lugar de duplicar campos.
                ProjectGenerator.generate(
                    targetDir = rootDir,
                    appName = moduleData.themesData.appName,
                    packageName = moduleData.packageName
                )

                // Los ficheros se escriben con java.io fuera del VFS; hay que marcar el
                // directorio como sucio para que el IDE recargue el estado real del disco
                // antes de vincular e importar el proyecto Gradle.
                val vRoot = LocalFileSystem.getInstance().findFileByIoFile(rootDir)
                if (vRoot != null) {
                    VfsUtil.markDirtyAndRefresh(true, true, true, vRoot)
                } else {
                    LocalFileSystem.getInstance().refreshIoFiles(listOf(rootDir))
                }
            }
        }
    }
