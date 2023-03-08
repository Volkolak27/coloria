package ru.binaryunicorn.coloria.common.enums

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.StringRes
import ru.binaryunicorn.coloria.R

enum class AnimationSpeed(@StringRes private val stringResId: Int) {
    // Ordinal dependent; Важен порядок, не перемешивать
    SLOW    (R.string.animation_speed_slow),
    MEDIUM  (R.string.animation_speed_medium),
    FAST    (R.string.animation_speed_fast),
    VERYFAST(R.string.animation_speed_veryfast);

    fun obtainString(context: Context): String = context.resources.getString(stringResId)

    companion object {
        fun fromOrdinal(ordinalNumber: Int): AnimationSpeed =
            values()[
                when {
                    ordinalNumber < 0 -> 0
                    ordinalNumber >= values().size -> values().lastIndex
                    else -> ordinalNumber
                }
            ]

        fun stringAdapter(context: Context): ArrayAdapter<String> {
            val items = values().map { it.obtainString(context) }

            val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, items)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            return arrayAdapter
        }
    }
}
