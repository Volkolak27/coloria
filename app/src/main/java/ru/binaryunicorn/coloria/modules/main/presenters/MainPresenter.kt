package ru.binaryunicorn.coloria.modules.main.presenters

import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.main.IMainPresenter
import ru.binaryunicorn.coloria.modules.main.IMainView
import ru.binaryunicorn.coloria.pattern.BasePresenter

class MainPresenter constructor(appSettings: IAppSettings) : BasePresenter<IMainView>(), IMainPresenter
{
    private val _appSettings: IAppSettings = appSettings

    //// IMainPresenter ////

    override fun fullscreenModeChanged(enabled: Boolean)
    {
        _appSettings.putFullScreenMode(enabled)
        view?.fullscreenMode(enabled)
    }

    override fun backAction()
    {
        if (_appSettings.isFullScreenMode())
        {
            _appSettings.putFullScreenMode(false)
            view?.fullscreenMode(false)
        }
        else
        {
            view?.back()
        }
    }

    override fun toAboutAction()
    {
        view?.showAboutDialog()
    }

    override fun alertMessageAction(msg: String)
    {
        view?.showAlertMessage(msg)
    }
}