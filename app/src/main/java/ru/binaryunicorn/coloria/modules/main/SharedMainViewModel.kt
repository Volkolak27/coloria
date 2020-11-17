package ru.binaryunicorn.coloria.modules.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedMainViewModel : ViewModel()
{
    private val _fullscreenMode = MutableLiveData<Boolean>()
    private val _toastMessage = MutableLiveData<String>()

    //// Public ////

    fun fullscreenMode(enabled: Boolean) { _fullscreenMode.value = enabled }
    fun fullscreenMode(): LiveData<Boolean> { return _fullscreenMode }

    fun toastMessage(msg: String) { _toastMessage.value = msg }
    fun toastMessage(): LiveData<String> { return _toastMessage }
}