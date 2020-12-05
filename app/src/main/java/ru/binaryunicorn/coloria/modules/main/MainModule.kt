package ru.binaryunicorn.coloria.modules.main

import dagger.Module
import dagger.Provides
import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.main.presenters.MainPresenter

@Module
class MainModule
{
    @Provides
    fun provideMainPresenter(appSettings: IAppSettings): IMainPresenter
    {
        return MainPresenter(appSettings)
    }
}