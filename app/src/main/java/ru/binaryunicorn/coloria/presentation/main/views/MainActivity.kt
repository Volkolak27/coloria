package ru.binaryunicorn.coloria.presentation.main.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.databinding.ActivityMainBinding
import ru.binaryunicorn.coloria.di.viewmodel.ViewModelFactory
import ru.binaryunicorn.coloria.presentation.about.views.AboutDialog
import ru.binaryunicorn.coloria.presentation.main.IMainSharedViewModel
import ru.binaryunicorn.coloria.presentation.main.IMainViewModel
import ru.binaryunicorn.coloria.presentation.main.viewmodels.MainSharedViewModel
import ru.binaryunicorn.coloria.presentation.main.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: IMainViewModel
    private lateinit var sharedViewModel: IMainSharedViewModel

    //// AppCompatActivity ////

    override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        sharedViewModel = ViewModelProvider(this, viewModelFactory)[MainSharedViewModel::class.java]

        initSetup()
    }

    //// Private ////

    private fun initSetup() {
        createActionMenu()

        sharedViewModel.apply {
            toastMessageSingleEvent.observe(this@MainActivity) { message -> showToast(message) }
            fullscreenMode.observe(this@MainActivity) { isFullscreen -> fullscreen(isFullscreen) }
            backSingleEvent.observe(this@MainActivity) { onBackPressedDispatcher.onBackPressed() }
        }

        viewModel.apply {
            toAboutSingleEvent.observe(this@MainActivity) { routeToAbout() }
        }
    }

    private fun createActionMenu() {
        addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.main, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.about_item -> {
                            viewModel.toAbout()
                            true
                        }
                        else -> false
                    }
                }
            }
        )
    }

    private fun showToast(text: String) {
        if (text.isNotEmpty()) {
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
        }
    }

    private fun fullscreen(enabled: Boolean) {
        supportActionBar?.let { if (enabled) it.hide() else it.show() }

        if (enabled) {
            onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        sharedViewModel.onBack()
                        remove()
                    }
                }
            )
        }
    }

    private fun routeToAbout() {
        AboutDialog.newInstance().show(supportFragmentManager, AboutDialog::class.simpleName)
    }
}
