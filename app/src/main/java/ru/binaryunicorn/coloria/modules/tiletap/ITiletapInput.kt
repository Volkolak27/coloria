package ru.binaryunicorn.coloria.modules.tiletap

interface ITiletapInput
{
    fun sendFullscreenMode(enabled: Boolean)
    fun recreateTiletapField()
}