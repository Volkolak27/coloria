package ru.binaryunicorn.coloria.pattern

import java.util.*

abstract class BasePresenter<T : IMvpView> : IMvpPresenter<T>
{
    private val _guid = UUID.randomUUID().toString()
    private var _view: T? = null

    companion object
    {
        const val PRESENTER_GUID: String = "PRESENTER_GUID"
    }

    //// IMvpPresenter ////

    override fun obtainGUID(): String
    {
        return _guid
    }

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

    override fun viewIsReady()
    {
        _view?.viewSelfSetup()
    }

    override fun destroy()
    {
        // empty
    }
}