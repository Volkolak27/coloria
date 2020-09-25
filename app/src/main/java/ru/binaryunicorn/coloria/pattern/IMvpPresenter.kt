package ru.binaryunicorn.coloria.pattern

interface IMvpPresenter<T : IMvpView>
{
    fun attachView(view: T)
    fun detachView()
    fun getView(): T?
    fun getGUID(): String

    fun viewIsReady()
    fun destroy()
}