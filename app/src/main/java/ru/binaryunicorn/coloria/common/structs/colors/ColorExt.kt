package ru.binaryunicorn.coloria.common.structs.colors

fun RgbColor.toFloatRgb(): FloatRgbColor = FloatRgbColor(red / 255.0f, green / 255.0f, blue / 255.0f)
fun FloatRgbColor.toRgbColor() = RgbColor((red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())

fun Array<Array<RgbColor>>.toFloatRgb(): Array<Array<FloatRgbColor>> =
    map { heightArray ->
        heightArray.map { widthArray ->
            widthArray.toFloatRgb()
        }.toTypedArray()
    }.toTypedArray()
