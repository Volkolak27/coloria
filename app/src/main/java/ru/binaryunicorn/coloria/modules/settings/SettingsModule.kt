package ru.binaryunicorn.coloria.modules.settings

import dagger.Module
import dagger.Provides
import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.settings.presenters.SettingsPresenter

@Module
class SettingsModule
{
    @Provides
    fun provideSettingsPresenter(appSettings: IAppSettings): ISettingsPresenter
    {
        return SettingsPresenter(appSettings)
    }
}