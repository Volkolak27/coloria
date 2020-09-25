package ru.binaryunicorn.coloria.modules.settings

import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.settings.presenters.SettingsPresenter
import ru.binaryunicorn.coloria.modules.settings.views.SettingsFragment

class SettingsModuleAssembly
{
    companion object
    {
        fun createFragment(appSettings: IAppSettings, output: ISettingsOutput?): Pair<SettingsFragment, SettingsPresenter>
        {
            val view = SettingsFragment.newInstance()
            val presenter = SettingsPresenter(view = view, appSettings = appSettings, output = output)

            view.inject(presenter = presenter)

            return Pair(view, presenter)
        }
    }
}