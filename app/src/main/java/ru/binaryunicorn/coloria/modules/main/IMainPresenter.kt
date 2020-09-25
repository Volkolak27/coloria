package ru.binaryunicorn.coloria.modules.main

import ru.binaryunicorn.coloria.pattern.IMvpPresenter

interface IMainPresenter : IMvpPresenter<IMainView>
{
    fun onBackAction()

    fun routeToTiletapScreen()
    fun routeToAbout()
}