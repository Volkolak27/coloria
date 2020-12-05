package ru.binaryunicorn.coloria.modules.tiletap

import dagger.Module
import dagger.Provides
import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.tiletap.presenters.TiletapPresenter

@Module
class TiletapModule
{
    @Provides
    fun provideTiletapPresenter(appSettings: IAppSettings): ITiletapPresenter
    {
        return TiletapPresenter(appSettings)
    }
}