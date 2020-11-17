package ru.binaryunicorn.coloria.modules.main

import ru.binaryunicorn.coloria.pattern.IMvpPresenter

interface IMainPresenter : IMvpPresenter<IMainView>
{
    fun fullscreenModeChanged(enabled: Boolean)

    fun backAction()
    fun toAboutAction()
    fun alertMessageAction(msg: String)
}