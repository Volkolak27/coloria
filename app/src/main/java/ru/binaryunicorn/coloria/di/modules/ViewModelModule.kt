package ru.binaryunicorn.coloria.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.binaryunicorn.coloria.di.viewmodel.ViewModelFactory
import ru.binaryunicorn.coloria.di.viewmodel.ViewModelKey
import ru.binaryunicorn.coloria.presentation.main.viewmodels.MainViewModel
import ru.binaryunicorn.coloria.presentation.settings.viewmodels.SettingsViewModel
import ru.binaryunicorn.coloria.presentation.tiles.viewmodels.TilesViewModel

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TilesViewModel::class)
    abstract fun bindTilesViewModel(viewModel: TilesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}
