package ru.binaryunicorn.coloria.modules.main.views

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.dialogs.AboutDialog
import ru.binaryunicorn.coloria.managers.appsettings.AppSetingsSPrefImpl
import ru.binaryunicorn.coloria.modules.ModuleStorage
import ru.binaryunicorn.coloria.modules.main.IMainInput
import ru.binaryunicorn.coloria.modules.main.IMainPresenter
import ru.binaryunicorn.coloria.modules.main.IMainView
import ru.binaryunicorn.coloria.modules.main.presenters.MainPresenter
import ru.binaryunicorn.coloria.modules.settings.ISettingsOutput
import ru.binaryunicorn.coloria.modules.settings.SettingsModuleAssembly
import ru.binaryunicorn.coloria.modules.settings.views.SettingsFragment
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapOutput
import ru.binaryunicorn.coloria.modules.tiletap.TiletapModuleAssembly
import ru.binaryunicorn.coloria.modules.tiletap.views.TiletapFragment
import ru.binaryunicorn.coloria.pattern.BasePresenter

class MainActivity : AppCompatActivity(), IMainView
{
	private var _presenter: IMainPresenter? = null

	//// IMvpView ////

	override fun bind(view: View)
	{
		// empty
	}

	override fun setupUserInteractions()
	{
		// empty
	}

	//// AppCompatActivity ////

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		if (savedInstanceState == null)
		{
			// SelfAssembly
			_presenter = MainPresenter(this, AppSetingsSPrefImpl(this))
			ModuleStorage.putPresenter(_presenter)
			_presenter?.routeToTiletapScreen()
		}
		else
		{
			savedInstanceState.getString(BasePresenter.PRESENTER_GUID)?.let {
				_presenter = ModuleStorage.obtainPresenter(it) as MainPresenter
				_presenter?.attachView(this)
			}
		}

		_presenter?.viewIsReady()
	}

	override fun onSaveInstanceState(outState: Bundle)
	{
		super.onSaveInstanceState(outState)
		outState.putString(BasePresenter.PRESENTER_GUID, _presenter?.getGUID())
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean
	{
		menuInflater.inflate(R.menu.action_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			R.id.action_about -> _presenter?.routeToAbout()
		}

		return super.onOptionsItemSelected(item)
	}

	override fun onBackPressed()
	{
		_presenter?.onBackAction()
	}

	//// IMainView ////

	override fun backAction()
	{
		super.onBackPressed()
	}

	override fun fullscreenMode(isFullscreen: Boolean)
	{
		supportActionBar?.let {
			if (isFullscreen) it.hide() else it.show()
		}
	}

	override fun openTiletap()
	{
		val module = TiletapModuleAssembly.createFragment(AppSetingsSPrefImpl(this), _presenter as ITiletapOutput)
		ModuleStorage.putPresenter(module.second)
		(_presenter as IMainInput).connect(tiletapInput = module.second)

		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, module.first, TiletapFragment::class.simpleName)
			.commit()
	}

	override fun openSettings()
	{
		val module = SettingsModuleAssembly.createFragment(AppSetingsSPrefImpl(this), _presenter as ISettingsOutput)
		ModuleStorage.putPresenter(module.second)

		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, module.first, SettingsFragment::class.simpleName)
			.addToBackStack(SettingsFragment::class.simpleName)
			.commit()
	}

	override fun openAbout()
	{
		AboutDialog.newInstance().show(supportFragmentManager, AboutDialog::class.simpleName)
	}

	override fun closeSettings()
	{
		supportFragmentManager.popBackStackImmediate()
	}

	override fun showFieldSizeError()
	{
		Toast.makeText(applicationContext, resources.getString(R.string.field_size_error), Toast.LENGTH_LONG).show()
	}
}