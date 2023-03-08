package ru.binaryunicorn.coloria.common.structs.colors

import android.util.Log
import ru.binaryunicorn.coloria.App

class RgbColor(red: Int, green: Int, blue: Int) {

    var red = red
        set(value) {
            field = when {
                value < 0 -> {
                    Log.e(App.LOGTAG, "Красный меньше 0 при работе с цветом")
                    0
                }
                value > 255 -> {
                    Log.e(App.LOGTAG, "Красный больше 255 при работе с цветом")
                    255
                }
                else -> value
            }
        }

    var green = green
        set(value) {
            field = when {
                value < 0 -> {
                    Log.e(App.LOGTAG, "Зеленый меньше 0 при работе с цветом")
                    0
                }
                value > 255 -> {
                    Log.e(App.LOGTAG, "Зеленый больше 255 при работе с цветом")
                    255
                }
                else -> value
            }
        }

    var blue = blue
        set(value) {
            field = when {
                value < 0 -> {
                    Log.e(App.LOGTAG, "Синий меньше 0 при работе с цветом")
                    0
                }
                value > 255 -> {
                    Log.e(App.LOGTAG, "Синий больше 255 при работе с цветом")
                    255
                }
                else -> value
            }
        }
}
