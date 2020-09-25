package ru.binaryunicorn.coloria.enums

import android.content.Context
import android.widget.ArrayAdapter
import ru.binaryunicorn.coloria.R

enum class AnimationSpeed(private val id: Int, private val stringResId: Int)
{
    SLOW    (0, R.string.animation_speed_slow),
    MEDIUM  (1, R.string.animation_speed_medium),
    FAST    (2, R.string.animation_speed_fast),
    VERYFAST(3, R.string.animation_speed_veryfast);

    companion object
    {
        fun fromId(id: Int): AnimationSpeed
        {
            return values()[id]
        }

        fun stringAdapter(context: Context): ArrayAdapter<String>
        {
            val items: MutableList<String> = mutableListOf()

            for (animationSpeed in values())
            {
                items.add( animationSpeed.obtainString(context) )
            }

            val arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            return arrayAdapter
        }
    }

    //// Public ////

    fun id(): Int = id
    fun obtainString(context: Context) = context.resources.getString(stringResId)
}