package ru.binaryunicorn.coloria.extra.structures

import android.util.Log
import ru.binaryunicorn.coloria.App

class RgbColor(red: Int, green: Int, blue: Int)
{
    //// Public ////

    var red = red
        set(value)
        {
            field = if (value < 0) {
                        Log.e(App.Consts.LOGTAG, "Красный меньше 0 При работе с цветом")
                        0
                    } else if (value > 255) {
                        Log.e(App.Consts.LOGTAG, "Красный больше 255 при работе с цветом")
                        255
                    } else {
                        value
                    }
        }

    var green = green
        set(value)
        {
            field = if (value < 0) {
                        Log.e(App.Consts.LOGTAG, "Зеленый меньше 0 при работе с цветом")
                        0
                    } else if (value > 255) {
                        Log.e(App.Consts.LOGTAG, "Зеленый больше 255 при работе с цветом")
                        255
                    } else {
                        value
                    }
        }

    var blue = blue
        set(value)
        {
            field = if (value < 0) {
                        Log.e(App.Consts.LOGTAG, "Синий меньше 0 при работе с цветом")
                        0
                    } else if (value > 255) {
                        Log.e(App.Consts.LOGTAG, "Синий больше 255 при работе с цветом")
                        255
                    } else {
                        value
                    }
        }

    fun toFloats() = FloatRgbColor((red / 255.0).toFloat(), (green / 255.0).toFloat(), (blue / 255.0).toFloat())

    //// Sub classes ////

    data class FloatRgbColor(val red: Float, val green: Float, val blue: Float)
}