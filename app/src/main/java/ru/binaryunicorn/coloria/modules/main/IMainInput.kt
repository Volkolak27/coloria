package ru.binaryunicorn.coloria.modules.main

import ru.binaryunicorn.coloria.modules.tiletap.ITiletapInput

interface IMainInput
{
    fun connect(tiletapInput: ITiletapInput?)
}