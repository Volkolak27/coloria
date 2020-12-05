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
        getView()?.fullscreenMode(enabled)
    }

    override fun backAction()
    {
        if (_appSettings.isFullScreenMode())
        {
            _appSettings.putFullScreenMode(false)
            getView()?.fullscreenMode(false)
        }
        else
        {
            getView()?.back()
        }
    }

    override fun toAboutAction()
    {
        getView()?.showAboutDialog()
    }

    override fun alertMessageAction(msg: String)
    {
        getView()?.showAlertMessage(msg)
    }
}