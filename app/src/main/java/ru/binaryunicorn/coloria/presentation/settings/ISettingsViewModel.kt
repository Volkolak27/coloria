package ru.binaryunicorn.coloria.presentation.settings

import ru.binaryunicorn.coloria.common.enums.AnimationSpeed
import ru.binaryunicorn.coloria.common.enums.AnimationType

interface ISettingsViewModel {
    // Properties
    var horizontalTilesCount: Int
    var verticalTilesCount: Int
    var isSoundEnabled: Boolean
    var isAnimationEnabled: Boolean
    var animationType: AnimationType
    var animationSpeed: AnimationSpeed

    // Input
    fun applySettings()
}
