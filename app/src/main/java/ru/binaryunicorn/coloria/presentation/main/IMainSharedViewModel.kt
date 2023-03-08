package ru.binaryunicorn.coloria.presentation.main

import androidx.lifecycle.LiveData
import ru.binaryunicorn.coloria.common.system.SingleLiveEvent

interface IMainSharedViewModel {
    // Output
    val toastMessageSingleEvent: SingleLiveEvent<String>
    val fullscreenMode: LiveData<Boolean>
    val backSingleEvent: SingleLiveEvent<Unit>

    // Input
    fun showToastMessage(msg: String)
    fun fullscreenMode(enabled: Boolean)
    fun onBack()
}
