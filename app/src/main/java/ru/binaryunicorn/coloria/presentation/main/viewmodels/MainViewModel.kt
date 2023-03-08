package ru.binaryunicorn.coloria.presentation.main.viewmodels

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import ru.binaryunicorn.coloria.common.system.SingleLiveEvent
import ru.binaryunicorn.coloria.presentation.main.IMainViewModel

class MainViewModel @Inject constructor() : ViewModel(), IMainViewModel {

    //// IMainViewModel ////

    override val toAboutSingleEvent = SingleLiveEvent<Unit>()

    override fun toAbout() {
        toAboutSingleEvent.postValue(Unit)
    }
}
