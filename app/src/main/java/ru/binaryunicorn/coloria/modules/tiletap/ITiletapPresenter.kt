package ru.binaryunicorn.coloria.modules.tiletap

import ru.binaryunicorn.coloria.pattern.IMvpPresenter

interface ITiletapPresenter : IMvpPresenter<ITiletapView>
{
    fun fieldSizeChanged(horizontalCount: Int, verticalCount: Int)
    fun rgbTestAction()
    fun fullscreenModeChanged(enabled: Boolean)
    fun tapSoundChanged(enabled: Boolean)
    fun animationChanged(enabled: Boolean)

    fun refreshTiletapFieldAction()
    fun resetTiletapField()

    fun toSettingsAction()
}