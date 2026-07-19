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
            // El asistente estándar de Android Studio ya pide Name / Package name /
            // Minimum SDK, así que se reutilizan esos valores en lugar de duplicar campos.
            ProjectGenerator.generate(
                targetDir = moduleData.projectTemplateData.rootDir,
                appName = moduleData.themesData.appName,
                packageName = moduleData.packageName
            )
        }
    }
