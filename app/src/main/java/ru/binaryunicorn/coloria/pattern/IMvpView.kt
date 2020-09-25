package ru.binaryunicorn.coloria.pattern

import android.view.View

interface IMvpView
{
    fun bind(view: View)
    fun setupUserInteractions()
}