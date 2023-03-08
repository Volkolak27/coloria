package ru.binaryunicorn.coloria

import android.app.Application
import ru.binaryunicorn.coloria.di.components.AppComponent
import ru.binaryunicorn.coloria.di.components.DaggerAppComponent
import ru.binaryunicorn.coloria.di.modules.AppSettingsModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appComponent =
            DaggerAppComponent
                .builder()
                .appSettingsModule(AppSettingsModule(applicationContext))
                .build()
    }

    companion object {
        const val LOGTAG = "ColorTag"
        lateinit var appComponent: AppComponent
    }
}
