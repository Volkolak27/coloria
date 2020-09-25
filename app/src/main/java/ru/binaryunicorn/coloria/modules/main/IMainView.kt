package ru.binaryunicorn.coloria.modules.main

import ru.binaryunicorn.coloria.pattern.IMvpView

interface IMainView : IMvpView
{
    fun backAction()
    fun fullscreenMode(isFullscreen: Boolean)

    fun openTiletap()
    fun openSettings()
    fun openAbout()
    fun closeSettings()

    fun showFieldSizeError()
}