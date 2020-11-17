package ru.binaryunicorn.coloria.modules.settings.presenters

import ru.binaryunicorn.coloria.extra.enums.AnimationSpeed
import ru.binaryunicorn.coloria.extra.enums.AnimationType
import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.settings.ISettingsPresenter
import ru.binaryunicorn.coloria.modules.settings.ISettingsView
import ru.binaryunicorn.coloria.pattern.BasePresenter
import javax.inject.Inject

class SettingsPresenter @Inject constructor(appSettings: IAppSettings) : BasePresenter<ISettingsView>(), ISettingsPresenter
{
    private val _appSettings: IAppSettings = appSettings

    private var _horizontalCount = _appSettings.obtainHorizontalCount()
    private var _verticalCount = _appSettings.obtainVerticalCount()
    private var _tapSoundEnable = _appSettings.isTapSoundEnabled()
    private var _animationEnabled = _appSettings.isAnimationEnabled()
    private var _animationType = _appSettings.obtainAnimationType()
    private var _animationSpeed = _appSettings.obtainAnimationSpeed()

    //// BasePresenter ////

    override fun viewIsReady()
    {
        super.viewIsReady()

        getView()?.updateHorizontalCount(_horizontalCount)
        getView()?.updateVerticalCount(_verticalCount)
        getView()?.updateTapSoundEnable(_tapSoundEnable)
        getView()?.updateAnimationEnable(_animationEnabled)
        getView()?.updateAnimationType(_animationType)
        getView()?.updateAnimationSpeed(_animationSpeed)
    }

    //// ISettingsPresenter ////

    override fun horizontalCountChanged(count: Int)
    {
        if (count > 0)
        {
            _horizontalCount = count
        }
    }

    override fun verticalCountChanged(count: Int)
    {
        if (count > 0)
        {
            _verticalCount = count
        }
    }

    override fun tapSoundEnableChanged(enabled: Boolean)
    {
        _tapSoundEnable = enabled
    }

    override fun animationEnableChanged(enabled: Boolean)
    {
        _animationEnabled = enabled
    }

    override fun animationTypeChanged(type: AnimationType)
    {
        _animationType = type
    }

    override fun animationSpeedChanged(speed: AnimationSpeed)
    {
        _animationSpeed = speed
    }

    override fun confirmAction()
    {
        with(_appSettings) {
            putHorizontalCount(_horizontalCount)
            putVerticalCount(_verticalCount)
            putTapSoundEnabled(_tapSoundEnable)
            putAnimationEnabled(_animationEnabled)
            putAnimationType(_animationType)
            putAnimationSpeed(_animationSpeed)
        }

        getView()?.confirm()
    }
}