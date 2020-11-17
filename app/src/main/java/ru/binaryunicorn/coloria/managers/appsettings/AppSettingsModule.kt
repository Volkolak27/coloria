package ru.binaryunicorn.coloria.managers.appsettings

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppSettingsModule(context: Context)
{
    private val _context = context

    @Provides fun provideAppSettings(): IAppSettings
    {
        return AppSetingsSPref(_context)
    }
}