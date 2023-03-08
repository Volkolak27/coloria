package ru.binaryunicorn.coloria.common.structs.colors

import android.util.Log
import ru.binaryunicorn.coloria.App

class FloatRgbColor(red: Float, green: Float, blue: Float) {

    var red = red
        set(value) {
            field = when {
                value < 0.0f -> {
                    Log.e(App.LOGTAG, "Красный меньше 0.0 при работе с цветом")
                    0.0f
                }
                value > 1.0f -> {
                    Log.e(App.LOGTAG, "Красный больше 1.0 при работе с цветом")
                    1.0f
                }
                else -> value
            }
        }

    var green = green
        set(value) {
            field = when {
                value < 0.0f -> {
                    Log.e(App.LOGTAG, "Зеленый меньше 0.0 при работе с цветом")
                    0.0f
                }
                value > 1.0f -> {
                    Log.e(App.LOGTAG, "Зеленый больше 1.0 при работе с цветом")
                    1.0f
                }
                else -> value
            }
        }

    var blue = blue
        set(value) {
            field = when {
                value < 0.0f -> {
                    Log.e(App.LOGTAG, "Синий меньше 0.0 при работе с цветом")
                    0.0f
                }
                value > 1.0f -> {
                    Log.e(App.LOGTAG, "Синий больше 1.0 при работе с цветом")
                    1.0f
                }
                else -> value
            }
        }
}
