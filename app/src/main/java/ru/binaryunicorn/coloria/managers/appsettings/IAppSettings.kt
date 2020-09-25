package ru.binaryunicorn.coloria.managers.appsettings

import ru.binaryunicorn.coloria.enums.AnimationSpeed
import ru.binaryunicorn.coloria.enums.AnimationType

interface IAppSettings
{
    fun putHorizontalCount(count: Int)
    fun obtainHorizontalCount(): Int

    fun putVerticalCount(count: Int)
    fun obtainVerticalCount(): Int

    fun putTapSoundEnabled(enabled: Boolean)
    fun isTapSoundEnabled(): Boolean

    fun putFullScreenMode(enabled: Boolean)
    fun isFullScreenMode(): Boolean

    fun putAnimationEnabled(enabled: Boolean)
    fun isAnimationEnabled(): Boolean

    fun putAnimationType(type: AnimationType)
    fun obtainAnimationType(): AnimationType

    fun putAnimationSpeed(speed: AnimationSpeed)
    fun obtainAnimationSpeed(): AnimationSpeed
}