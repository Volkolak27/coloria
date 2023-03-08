package ru.binaryunicorn.coloria.presentation.tiles.viewmodels

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import ru.binaryunicorn.coloria.Consts
import ru.binaryunicorn.coloria.common.enums.AnimationSpeed
import ru.binaryunicorn.coloria.common.enums.AnimationType
import ru.binaryunicorn.coloria.common.system.SingleLiveEvent
import ru.binaryunicorn.coloria.domain.settings.IAppSettings
import ru.binaryunicorn.coloria.presentation.tiles.ITilesViewModel

class TilesViewModel @Inject constructor(
    private val appSettings: IAppSettings
) : ViewModel(), ITilesViewModel {

    private var isViewInited = false
    private var lastTilesNeedToRecreateEventUuid = ""

    //// ITilesViewModel ////

    override val fieldSize = SingleLiveEvent<Pair<Int, Int>>()
    override val soundEnabled = SingleLiveEvent<Boolean>()
    override val animation = SingleLiveEvent<Pair<Long, Float>?>()
    override val toFullscreenSingleEvent = SingleLiveEvent<Boolean>()
    override val toRgbTestSingleEvent = SingleLiveEvent<Unit>()
    override val toSettingsSingleEvent = SingleLiveEvent<Unit>()

    override fun onViewCreate() {
        if (!isViewInited) {
            fieldSize.postValue(
                Pair(appSettings.obtainHorizontalTilesCount(), appSettings.obtainVerticalTilesCount())
            )
            soundEnabled.postValue(appSettings.isTapSoundEnabled())
            animation.postValue(
                if (appSettings.isAnimationEnabled()) {
                    Pair(
                        appSettings.obtainAnimationSpeed().toMilliseconds(),
                        appSettings.obtainAnimationType().toPercent()
                    )
                } else {
                    null
                }
            )

            isViewInited = true
        }
    }

    override fun changeFieldSize(horizontalCount: Int, verticalCount: Int) {
        appSettings.putHorizontalTilesCount(horizontalCount)
        appSettings.putVerticalTilesCount(verticalCount)
        fieldSize.postValue(Pair(horizontalCount, verticalCount))
    }

    override fun changeTapSound(enabled: Boolean) {
        appSettings.putTapSoundEnabled(enabled)
        soundEnabled.postValue(enabled)
    }

    override fun changeAnimation(enabled: Boolean) {
        appSettings.putAnimationEnabled(enabled)

        animation.postValue(
            if (enabled) {
                Pair(
                    appSettings.obtainAnimationSpeed().toMilliseconds(),
                    appSettings.obtainAnimationType().toPercent()
                )
            } else {
                null
            }
        )
    }

    override fun changeFullscreenMode(enabled: Boolean) {
        appSettings.putFullScreenEnabled(enabled)
        toFullscreenSingleEvent.postValue(enabled)
    }

    override fun toSettings() {
        toSettingsSingleEvent.postValue(Unit)
    }

    override fun toRgbTest() {
        toRgbTestSingleEvent.postValue(Unit)
    }

    override fun tilesNeedToRecreate(eventUuid: String) {
        if (lastTilesNeedToRecreateEventUuid != eventUuid) {
            fieldSize.postValue(
                Pair(
                    appSettings.obtainHorizontalTilesCount(),
                    appSettings.obtainVerticalTilesCount()
                )
            )
            soundEnabled.postValue(appSettings.isTapSoundEnabled())

            animation.postValue(
                if (appSettings.isAnimationEnabled()) {
                    Pair(
                        appSettings.obtainAnimationSpeed().toMilliseconds(),
                        appSettings.obtainAnimationType().toPercent()
                    )
                } else {
                    null
                }
            )

            lastTilesNeedToRecreateEventUuid = eventUuid
        }
    }

    override fun resetTiletapField() {
        appSettings.putHorizontalTilesCount(Consts.DEFAULT_HORIZONTAL_TILES_COUNT)
        appSettings.putVerticalTilesCount(Consts.DEFAULT_VERTICAL_TILES_COUNT)
        fieldSize.postValue(
            Pair(
                Consts.DEFAULT_HORIZONTAL_TILES_COUNT,
                Consts.DEFAULT_VERTICAL_TILES_COUNT
            )
        )
    }

    //// Private ////

    private fun AnimationType.toPercent(): Float {
        return when (this) {
            AnimationType.LOW  -> 0.25f
            AnimationType.HALF -> 0.50f
            AnimationType.BIG  -> 0.75f
            AnimationType.FULL -> 1.00f
        }
    }

    private fun AnimationSpeed.toMilliseconds(): Long {
        return when (this) {
            AnimationSpeed.SLOW     -> 2000L
            AnimationSpeed.MEDIUM   -> 1200L
            AnimationSpeed.FAST     -> 600L
            AnimationSpeed.VERYFAST -> 200L
        }
    }
}
