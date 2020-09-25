package ru.binaryunicorn.coloria.modules.main.presenters

import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.main.IMainInput
import ru.binaryunicorn.coloria.modules.main.IMainPresenter
import ru.binaryunicorn.coloria.modules.main.IMainView
import ru.binaryunicorn.coloria.modules.settings.ISettingsOutput
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapInput
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapOutput
import ru.binaryunicorn.coloria.pattern.BasePresenter

class MainPresenter(view: IMainView, appSettings: IAppSettings) : BasePresenter<IMainView>(view), IMainPresenter, IMainInput, ITiletapOutput, ISettingsOutput
{
    private val _appSettings: IAppSettings = appSettings
    private var _tiletapInput: ITiletapInput? = null

    //// BasePresenter ////

    override fun viewIsReady()
    {
        super.viewIsReady()
    }

    //// IMainPresenter ////

    override fun onBackAction()
    {
        if (_appSettings.isFullScreenMode())
        {
            _appSettings.putFullScreenMode(false)
            getView()?.fullscreenMode(false)
            _tiletapInput?.sendFullscreenMode(false)
        }
        else
        {
            getView()?.backAction()
        }
    }

    override fun routeToTiletapScreen()
    {
        val horizontalSize = _appSettings.obtainHorizontalCount()
        val verticalSize = _appSettings.obtainVerticalCount()

        if (horizontalSize >= 4 || verticalSize >= 4)
        {
            _appSettings.putHorizontalCount(4)
            _appSettings.putVerticalCount(4)
        }

        getView()?.openTiletap()
    }

    override fun routeToAbout()
    {
        getView()?.openAbout()
    }

    //// IMainInput ////

    override  fun connect(tiletapInput: ITiletapInput?)
    {
        _tiletapInput = tiletapInput
    }

    //// ITiletapOutput ////

    override fun requestFullscreenMode()
    {
        _appSettings.putFullScreenMode(true)
        getView()?.fullscreenMode(true)
    }

    override fun routeToSettings()
    {
        getView()?.openSettings()
    }

    override fun showFieldSizeError()
    {
        getView()?.showFieldSizeError()
    }

    //// ISettingsOutput ////

    override fun confirmSettingsAction()
    {
        getView()?.closeSettings()
        _tiletapInput?.recreateTiletapField()
    }
}