package ru.binaryunicorn.coloria.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.binaryunicorn.coloria.domain.settings.AppSettingsSPref
import ru.binaryunicorn.coloria.domain.settings.IAppSettings

@Module
class AppSettingsModule(private val context: Context) {

    @Provides
    fun provideAppSettings(): IAppSettings {
        return AppSettingsSPref(context)
    }
}
