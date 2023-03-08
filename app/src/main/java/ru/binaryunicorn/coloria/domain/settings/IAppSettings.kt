package ru.binaryunicorn.coloria.domain.settings

import ru.binaryunicorn.coloria.common.enums.AnimationSpeed
import ru.binaryunicorn.coloria.common.enums.AnimationType

interface IAppSettings {
    fun putHorizontalTilesCount(count: Int)
    fun obtainHorizontalTilesCount(): Int

    fun putVerticalTilesCount(count: Int)
    fun obtainVerticalTilesCount(): Int

    fun putTapSoundEnabled(enabled: Boolean)
    fun isTapSoundEnabled(): Boolean

    fun putFullScreenEnabled(enabled: Boolean)
    fun isFullScreenEnabled(): Boolean

    fun putAnimationEnabled(enabled: Boolean)
    fun isAnimationEnabled(): Boolean

    fun putAnimationType(type: AnimationType)
    fun obtainAnimationType(): AnimationType

    fun putAnimationSpeed(speed: AnimationSpeed)
    fun obtainAnimationSpeed(): AnimationSpeed
}
