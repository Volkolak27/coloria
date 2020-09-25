package ru.binaryunicorn.coloria.modules.tiletap

import ru.binaryunicorn.coloria.pattern.IMvpView

interface ITiletapView : IMvpView
{
    fun updateFieldSize(horizontalCount: Int, verticalCount: Int)
    fun updateRandomColorForTile(horizontalPos: Int, verticalPos: Int)
    fun updateFullscreenMode(enabled: Boolean)
    fun updateTapSound(enabled: Boolean)
    fun updateAnimation(enabled: Boolean)

    fun refreshTiletapField()
}