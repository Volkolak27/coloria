package ru.binaryunicorn.coloria.pattern

interface IMvpPresenter<T : IMvpView>
{
    var view: T?

    fun obtainGUID(): String
    fun attachView(view: T)
    fun detachView()

    fun viewIsReady()
    fun destroy()
}