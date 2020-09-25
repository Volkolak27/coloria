package ru.binaryunicorn.coloria.pattern

import java.util.*

abstract class BasePresenter<T : IMvpView>(view: T?) : IMvpPresenter<T>
{
    private var _view: T? = view
    private val _guid: String = UUID.randomUUID().toString()

    companion object
    {
        const val PRESENTER_GUID: String = "PRESENTER_GUID"
    }

    //// IMvpPresenter ////

    override fun attachView(view: T)
    {
        _view = view
    }

    override fun detachView()
    {
        _view = null
    }

    override fun getView(): T?
    {
        return _view
    }

    override fun getGUID(): String
    {
        return _guid
    }

    override fun viewIsReady()
    {
        // empty
    }

    override fun destroy()
    {
        // empty
    }
}