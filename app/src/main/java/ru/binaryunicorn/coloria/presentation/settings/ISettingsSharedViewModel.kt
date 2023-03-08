package ru.binaryunicorn.coloria.presentation.settings

import androidx.lifecycle.LiveData
import java.util.UUID

interface ISettingsSharedViewModel {
    // Output
    val settingsDidChangeAndNeedReload: LiveData<UUID>

    // Input
    fun onSettingsDidChangeAndNeedReload()
}
