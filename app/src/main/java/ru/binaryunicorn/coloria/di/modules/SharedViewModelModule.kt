package ru.binaryunicorn.coloria.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton
import ru.binaryunicorn.coloria.di.viewmodel.ViewModelKey
import ru.binaryunicorn.coloria.presentation.main.viewmodels.MainSharedViewModel
import ru.binaryunicorn.coloria.presentation.settings.viewmodels.SettingsSharedViewModel

@Module
abstract class SharedViewModelModule {

    @Binds
    @Singleton
    @IntoMap
    @ViewModelKey(MainSharedViewModel::class)
    abstract fun bindMainSharedViewModel(viewModel: MainSharedViewModel): ViewModel

    @Binds
    @Singleton
    @IntoMap
    @ViewModelKey(SettingsSharedViewModel::class)
    abstract fun bindSharedSettingsViewModel(viewModel: SettingsSharedViewModel): ViewModel
}
