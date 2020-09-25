package ru.binaryunicorn.coloria.pattern

interface IModuleStorage
{
    fun <T> putPresenter(presenter: T)
    fun obtainPresenter(guid: String): Any?
    fun removePresenter(guid: String)
    fun removeAllPresenters()
}