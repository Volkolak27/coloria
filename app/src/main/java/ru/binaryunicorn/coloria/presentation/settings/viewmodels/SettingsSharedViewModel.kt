package ru.binaryunicorn.coloria.presentation.settings.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID
import javax.inject.Inject
import ru.binaryunicorn.coloria.presentation.settings.ISettingsSharedViewModel

class SettingsSharedViewModel @Inject constructor() : ViewModel(), ISettingsSharedViewModel {

    //// ISettingsSharedViewModel ////

    override val settingsDidChangeAndNeedReload = MutableLiveData<UUID>()

    override fun onSettingsDidChangeAndNeedReload() {
        settingsDidChangeAndNeedReload.postValue(UUID.randomUUID())
    }
}
