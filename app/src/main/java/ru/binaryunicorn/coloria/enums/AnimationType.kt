package ru.binaryunicorn.coloria.enums

import android.content.Context
import android.widget.ArrayAdapter
import ru.binaryunicorn.coloria.R

enum class AnimationType(private val id: Int, private val stringResId: Int)
{
    LOW (0, R.string.animation_type_low),
    HALF(1, R.string.animation_type_half),
    BIG (2, R.string.animation_type_big),
    FULL(3, R.string.animation_type_full);

    companion object
    {
        fun fromId(id: Int): AnimationType
        {
            return values()[id]
        }

        fun stringAdapter(context: Context): ArrayAdapter<String>
        {
            val items: MutableList<String> = mutableListOf()

            for (animationType in values())
            {
                items.add( animationType.obtainString(context) )
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