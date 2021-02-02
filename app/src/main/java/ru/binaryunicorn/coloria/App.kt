package ru.binaryunicorn.coloria

import android.app.Application
import ru.binaryunicorn.coloria.managers.appsettings.AppSettingsModule

class App : Application()
{
    companion object
    {
        const val LOGTAG = "ColoriaTag"
        lateinit var appComponent: AppComponent
    }

    class Consts
    {
        companion object
        {
            const val DEFAULT_TILES_COUNT_ON_HORIZONTAL = 4
            const val DEFAULT_TILES_COUNT_ON_VERTICAL = 4

            const val FLOAT_SIZEOF = 4
        }
    }

    override fun onCreate()
    {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appSettingsModule( AppSettingsModule(applicationContext) ).build()
    }
}