package ru.binaryunicorn.coloria

import dagger.Component
import ru.binaryunicorn.coloria.managers.appsettings.AppSettingsModule
import ru.binaryunicorn.coloria.modules.main.MainModule
import ru.binaryunicorn.coloria.modules.main.views.MainActivity
import ru.binaryunicorn.coloria.modules.settings.SettingsModule
import ru.binaryunicorn.coloria.modules.settings.views.SettingsFragment
import ru.binaryunicorn.coloria.modules.tiletap.TiletapModule
import ru.binaryunicorn.coloria.modules.tiletap.views.TiletapFragment

@Component (modules = [AppSettingsModule::class, MainModule::class, TiletapModule::class, SettingsModule::class])
interface AppComponent
{
    fun inject(target: MainActivity)
    fun inject(target: TiletapFragment)
    fun inject(target: SettingsFragment)
}