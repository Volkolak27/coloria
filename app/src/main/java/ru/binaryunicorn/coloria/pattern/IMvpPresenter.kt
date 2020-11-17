package ru.binaryunicorn.coloria.pattern

interface IMvpPresenter<T : IMvpView>
{
    fun obtainGUID(): String
    fun attachView(view: T)
    fun detachView()
    fun getView(): T?

    fun viewIsReady()
    fun destroy()
}