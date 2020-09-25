package ru.binaryunicorn.coloria.modules.tiletap

import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.tiletap.presenters.TiletapPresenter
import ru.binaryunicorn.coloria.modules.tiletap.views.TiletapFragment

class TiletapModuleAssembly
{
    companion object
    {
        fun createFragment(appSettings: IAppSettings, output: ITiletapOutput?): Pair<TiletapFragment, TiletapPresenter>
        {
            val view = TiletapFragment.newInstance()
            val presenter = TiletapPresenter(view = view, appSettings = appSettings, output = output)

            view.inject(presenter = presenter)

            return Pair(view, presenter)
        }
    }
}