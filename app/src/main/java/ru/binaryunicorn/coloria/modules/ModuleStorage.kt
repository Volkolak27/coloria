package ru.binaryunicorn.coloria.modules

import ru.binaryunicorn.coloria.modules.main.presenters.MainPresenter
import ru.binaryunicorn.coloria.modules.settings.presenters.SettingsPresenter
import ru.binaryunicorn.coloria.modules.tiletap.presenters.TiletapPresenter
import ru.binaryunicorn.coloria.pattern.IModuleStorage

object ModuleStorage : IModuleStorage
{
    private var _mainPresenter: MainPresenter? = null
    private var _tiletapPresenter: TiletapPresenter? = null
    private var _settingsPresenter: SettingsPresenter? = null

    //// IModuleStorage ////

    override fun <T> putPresenter(presenter: T)
    {
        when (presenter)
        {
            is MainPresenter -> _mainPresenter = presenter
            is TiletapPresenter -> _tiletapPresenter = presenter
            is SettingsPresenter -> _settingsPresenter = presenter
        }
    }

    override fun obtainPresenter(guid: String): Any?
    {
        return when (guid) {
            _mainPresenter?.getGUID() -> _mainPresenter
            _tiletapPresenter?.getGUID() -> _tiletapPresenter
            _settingsPresenter?.getGUID() -> _settingsPresenter
            else -> null
        }
    }

    override fun removePresenter(guid: String)
    {
        if (_mainPresenter?.getGUID() == guid) { _mainPresenter = null }
        if (_tiletapPresenter?.getGUID() == guid) { _tiletapPresenter = null }
        if (_settingsPresenter?.getGUID() == guid) { _settingsPresenter = null }
    }

    override fun removeAllPresenters()
    {
        _mainPresenter = null
        _tiletapPresenter = null
        _settingsPresenter = null
    }
}