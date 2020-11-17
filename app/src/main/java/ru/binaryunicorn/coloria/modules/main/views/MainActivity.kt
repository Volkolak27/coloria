package ru.binaryunicorn.coloria.modules.main.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.databinding.MainActivityBinding
import ru.binaryunicorn.coloria.dialogs.AboutDialog
import ru.binaryunicorn.coloria.modules.main.IMainView
import ru.binaryunicorn.coloria.modules.main.SharedMainViewModel
import ru.binaryunicorn.coloria.modules.main.presenters.MainPresenter
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IMainView
{
	@Inject lateinit var presenter: MainPresenter
	override var initializated = false
	private lateinit var _binding: MainActivityBinding
	private val _sharedMainViewModel: SharedMainViewModel by viewModels()

	//// IMvpView ////

	override fun viewSelfSetup()
	{
		_sharedMainViewModel.fullscreenMode().observe(this,
			Observer<Boolean>
			{
				presenter.fullscreenModeChanged(it)
			}
		)
		_sharedMainViewModel.toastMessage().observe(this,
			Observer<String>
			{
				if (initializated)
				{
					presenter.alertMessageAction(it)
				}
			}
		)
	}

	//// AppCompatActivity ////

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		_binding = MainActivityBinding.inflate(layoutInflater)
		setContentView(_binding.root)
		App.appComponent.inject(this)

		presenter.attachView(this)
		presenter.viewIsReady()
	}

	override fun onResume()
	{
		super.onResume()
		initializated = true
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
			R.id.action_about -> presenter.toAboutAction()
		}

		return super.onOptionsItemSelected(item)
	}

	override fun onBackPressed()
	{
		presenter.backAction()
	}

	//// IMainView ////

	override fun fullscreenMode(enabled: Boolean)
	{
		supportActionBar?.let {
			if (enabled) it.hide() else it.show()
		}
	}

	override fun back()
	{
		super.onBackPressed()
	}

	override fun showAboutDialog()
	{
		AboutDialog.newInstance().show(supportFragmentManager, AboutDialog::class.simpleName)
	}

	override fun showAlertMessage(msg: String)
	{
		Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
	}
}