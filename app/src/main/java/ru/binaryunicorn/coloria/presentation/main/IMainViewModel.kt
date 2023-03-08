package ru.binaryunicorn.coloria.presentation.main

import ru.binaryunicorn.coloria.common.system.SingleLiveEvent

interface IMainViewModel {
    // Output
    val toAboutSingleEvent: SingleLiveEvent<Unit>

    // Input
    fun toAbout()
}
