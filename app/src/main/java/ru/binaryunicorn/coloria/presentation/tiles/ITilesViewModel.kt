package ru.binaryunicorn.coloria.presentation.tiles

import ru.binaryunicorn.coloria.common.system.SingleLiveEvent

interface ITilesViewModel {
    // Output
    val fieldSize: SingleLiveEvent<Pair<Int, Int>> // по горизонтали, по вертикали
    val soundEnabled: SingleLiveEvent<Boolean>
    val animation: SingleLiveEvent<Pair<Long, Float>?> // скорость, тип
    val toFullscreenSingleEvent: SingleLiveEvent<Boolean>
    val toRgbTestSingleEvent: SingleLiveEvent<Unit>
    val toSettingsSingleEvent: SingleLiveEvent<Unit>

    // Input
    fun onViewCreate()
    fun changeFieldSize(horizontalCount: Int, verticalCount: Int)
    fun changeTapSound(enabled: Boolean)
    fun changeAnimation(enabled: Boolean)
    fun changeFullscreenMode(enabled: Boolean)
    fun toSettings()
    fun toRgbTest()

    // Methods
    fun tilesNeedToRecreate(eventUuid: String)
    fun resetTiletapField()
}
