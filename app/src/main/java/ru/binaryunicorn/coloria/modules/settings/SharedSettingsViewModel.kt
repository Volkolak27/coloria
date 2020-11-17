package ru.binaryunicorn.coloria.modules.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SharedSettingsViewModel : ViewModel()
{
    private val _settingsChanged = MutableLiveData<String>()

    //// Public ////

    fun settingsDidChange() { _settingsChanged.value = UUID.randomUUID().toString() }
    fun settingsChanged(): LiveData<String> { return _settingsChanged }
}