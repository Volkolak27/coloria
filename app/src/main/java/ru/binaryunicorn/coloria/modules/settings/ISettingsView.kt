package ru.binaryunicorn.coloria.modules.settings

import ru.binaryunicorn.coloria.extra.enums.AnimationSpeed
import ru.binaryunicorn.coloria.extra.enums.AnimationType
import ru.binaryunicorn.coloria.pattern.IMvpView

interface ISettingsView : IMvpView
{
    fun updateHorizontalCount(count: Int)
    fun updateVerticalCount(count: Int)
    fun updateTapSoundEnable(enabled: Boolean)
    fun updateAnimationEnable(enabled: Boolean)
    fun updateAnimationType(type: AnimationType)
    fun updateAnimationSpeed(speed: AnimationSpeed)

    fun confirm()
}