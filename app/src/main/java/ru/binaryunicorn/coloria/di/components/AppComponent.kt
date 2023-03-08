package ru.binaryunicorn.coloria.di.components

import dagger.Component
import javax.inject.Singleton
import ru.binaryunicorn.coloria.di.modules.AppSettingsModule
import ru.binaryunicorn.coloria.di.modules.SharedViewModelModule
import ru.binaryunicorn.coloria.di.modules.ViewModelModule
import ru.binaryunicorn.coloria.presentation.main.views.MainActivity
import ru.binaryunicorn.coloria.presentation.settings.views.SettingsFragment
import ru.binaryunicorn.coloria.presentation.tiles.views.TilesFragment

@Singleton
@Component(
    modules = [
        AppSettingsModule::class,
        SharedViewModelModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: TilesFragment)
    fun inject(target: SettingsFragment)
}
