package ru.binaryunicorn.coloria.presentation.settings.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.domain.settings.IAppSettings
import ru.binaryunicorn.coloria.presentation.settings.ISettingsViewModel

class SettingsViewModel @Inject constructor(
    private val appSettings: IAppSettings
) : ViewModel(), ISettingsViewModel {

    //// ISettingsViewModel ////

    override var horizontalTilesCount = appSettings.obtainHorizontalTilesCount()
        set(value) {
            field = if (value <= 0) {
                Log.e(App.LOGTAG, "Значение по ширине не может быть <= 0")
                1
            } else {
                value
            }
        }
    override var verticalTilesCount = appSettings.obtainVerticalTilesCount()
        set(value) {
            field = if (value <= 0) {
                Log.e(App.LOGTAG, "Значение по высоте не может быть <= 0")
                1
            } else {
                value
            }
        }
    override var isSoundEnabled = appSettings.isTapSoundEnabled()
    override var isAnimationEnabled = appSettings.isAnimationEnabled()
    override var animationType = appSettings.obtainAnimationType()
    override var animationSpeed = appSettings.obtainAnimationSpeed()

    override fun applySettings() {
        with(appSettings) {
            putHorizontalTilesCount(horizontalTilesCount)
            putVerticalTilesCount(verticalTilesCount)
            putTapSoundEnabled(isSoundEnabled)
            putAnimationEnabled(isAnimationEnabled)
            putAnimationType(animationType)
            putAnimationSpeed(animationSpeed)
        }
    }
}
