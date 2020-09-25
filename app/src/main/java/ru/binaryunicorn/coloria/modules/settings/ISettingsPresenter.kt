package ru.binaryunicorn.coloria.modules.settings

import ru.binaryunicorn.coloria.enums.AnimationSpeed
import ru.binaryunicorn.coloria.enums.AnimationType
import ru.binaryunicorn.coloria.pattern.IMvpPresenter

interface ISettingsPresenter : IMvpPresenter<ISettingsView>
{
    fun horizontalCountChanged(count: Int)
    fun verticalCountChanged(count: Int)
    fun tapSoundEnableChanged(enabled: Boolean)
    fun animationEnableChanged(enabled: Boolean)
    fun animationTypeChanged(type: AnimationType)
    fun animationSpeedChanged(speed: AnimationSpeed)

    fun confirmAction()
}