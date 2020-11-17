package ru.binaryunicorn.coloria.modules.main

import ru.binaryunicorn.coloria.pattern.IMvpView

interface IMainView : IMvpView
{
    fun fullscreenMode(enabled: Boolean)

    fun back()
    fun showAboutDialog()
    fun showAlertMessage(msg: String)
}