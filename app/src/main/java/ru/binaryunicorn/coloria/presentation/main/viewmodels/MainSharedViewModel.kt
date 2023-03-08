package ru.binaryunicorn.coloria.presentation.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import ru.binaryunicorn.coloria.common.system.SingleLiveEvent
import ru.binaryunicorn.coloria.domain.settings.IAppSettings
import ru.binaryunicorn.coloria.presentation.main.IMainSharedViewModel

class MainSharedViewModel @Inject constructor(
    private val appSettings: IAppSettings
) : ViewModel(), IMainSharedViewModel {

    //// IMainSharedViewModel ////

    override val toastMessageSingleEvent = SingleLiveEvent<String>()
    override val fullscreenMode = MutableLiveData(appSettings.isFullScreenEnabled())
    override val backSingleEvent = SingleLiveEvent<Unit>()

    override fun showToastMessage(msg: String) {
        toastMessageSingleEvent.postValue(msg)
    }

    override fun fullscreenMode(enabled: Boolean) {
        appSettings.putFullScreenEnabled(enabled)
        fullscreenMode.postValue(enabled)
    }

    override fun onBack() {
        if (fullscreenMode.value == true) {
            fullscreenMode(false)
        } else {
            backSingleEvent.postValue(Unit)
        }
    }
}
