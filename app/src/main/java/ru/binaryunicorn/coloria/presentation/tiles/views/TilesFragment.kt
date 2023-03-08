package ru.binaryunicorn.coloria.presentation.tiles.views

import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.databinding.FragmentTilesBinding
import ru.binaryunicorn.coloria.di.viewmodel.ViewModelFactory
import ru.binaryunicorn.coloria.presentation.main.IMainSharedViewModel
import ru.binaryunicorn.coloria.presentation.main.viewmodels.MainSharedViewModel
import ru.binaryunicorn.coloria.presentation.settings.ISettingsSharedViewModel
import ru.binaryunicorn.coloria.presentation.settings.viewmodels.SettingsSharedViewModel
import ru.binaryunicorn.coloria.presentation.tiles.ITilesViewModel
import ru.binaryunicorn.coloria.presentation.tiles.viewmodels.TilesViewModel

class TilesFragment : Fragment() {

    private var binding: FragmentTilesBinding? = null

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ITilesViewModel
    private lateinit var mainSharedViewModel: IMainSharedViewModel
    private lateinit var sharedSettingsViewModel: ISettingsSharedViewModel

    private var actionMenu: Menu? = null
    private var tapSoundChecked = false
    private var animationChecked = false

    //// Fragment ////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTilesBinding.inflate(inflater, container, false)

        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[TilesViewModel::class.java]
        mainSharedViewModel = ViewModelProvider(this, viewModelFactory)[MainSharedViewModel::class.java]
        sharedSettingsViewModel = ViewModelProvider(this, viewModelFactory)[SettingsSharedViewModel::class.java]

        initSetup(savedInstanceState)
        viewModel.onViewCreate()

        return binding?.root
    }

    override fun onResume() {
        super.onResume()

        val soundPool = SoundPool.Builder().also { it.setMaxStreams(10) }.build()
        val sounds = intArrayOf(
            soundPool.load(context, R.raw.tap01, 1),
            soundPool.load(context, R.raw.tap02, 1),
            soundPool.load(context, R.raw.tap03, 1)
        )

        binding?.tiles?.updateSounds(soundPool, sounds)
    }

    override fun onPause() {
        super.onPause()
        binding?.tiles?.releaseSounds()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_TAP_SOUND_CHECKED, tapSoundChecked)
        outState.putBoolean(KEY_ANIMATION_CHECKED, animationChecked)
    }

    //// Private ////

    private fun initSetup(savedInstanceState: Bundle?) {
        restoreState(savedInstanceState)
        createActionMenu()

        viewModel.apply {
            fieldSize.observe(viewLifecycleOwner) { (width, height) ->
                try {
                    binding?.tiles?.isRgbMode = false
                    binding?.tiles?.generateField(width, height)
                } catch (e: OutOfMemoryError) {
                    Log.e(App.LOGTAG, "Размер поля: $width на $height - вызвал ошибку OutOfMemoryError")
                    mainSharedViewModel.showToastMessage(getString(R.string.field_size_error))
                    viewModel.resetTiletapField()
                }
            }

            soundEnabled.observe(viewLifecycleOwner) { enabled ->
                tapSoundChecked = enabled
                actionMenu?.findItem(R.id.tap_sound_item)?.isChecked = tapSoundChecked
                binding?.tiles?.isSoundEnabled = enabled
            }

            animation.observe(viewLifecycleOwner) { params ->
                if (params != null) {
                    animationChecked = true
                    binding?.tiles?.enableAnimation(params.first, params.second)
                } else {
                    animationChecked = false
                    binding?.tiles?.disableAnimation()
                }

                actionMenu?.findItem(R.id.animation_item)?.isChecked = animationChecked
            }

            toFullscreenSingleEvent.observe(viewLifecycleOwner) { enabled ->
                changeFullscreen(enabled)
            }

            toRgbTestSingleEvent.observe(viewLifecycleOwner) {
                binding?.tiles?.isRgbMode = true
            }

            toSettingsSingleEvent.observe(viewLifecycleOwner) {
                findNavController().navigate(R.id.action_tilesFragment_to_settingsFragment)
            }
        }

        sharedSettingsViewModel.apply {
            settingsDidChangeAndNeedReload.observe(viewLifecycleOwner) { uuid ->
                viewModel.tilesNeedToRecreate(uuid.toString())
            }
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { bundle ->
            tapSoundChecked = bundle.getBoolean(KEY_TAP_SOUND_CHECKED)
            animationChecked = bundle.getBoolean(KEY_ANIMATION_CHECKED)
        }
    }

    private fun createActionMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.tiles, menu)

                    actionMenu = menu.apply {
                        findItem(R.id.tap_sound_item).isChecked = tapSoundChecked
                        findItem(R.id.animation_item).isChecked = animationChecked
                    }
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.change_to_1x1_item -> {
                            viewModel.changeFieldSize(1, 1)
                            true
                        }
                        R.id.change_to_2x2_item -> {
                            viewModel.changeFieldSize(2, 2)
                            true
                        }
                        R.id.change_to_16x16_item -> {
                            viewModel.changeFieldSize(16, 16)
                            true
                        }
                        R.id.rgb_test_item -> {
                            viewModel.toRgbTest()
                            true
                        }
                        R.id.to_settings_item -> {
                            viewModel.toSettings()
                            true
                        }
                        R.id.full_screen_item -> {
                            actionMenu?.findItem(menuItem.itemId)?.let { viewModel.changeFullscreenMode(true) }
                            true
                        }
                        R.id.tap_sound_item -> {
                            actionMenu?.findItem(menuItem.itemId)?.let { viewModel.changeTapSound(!it.isChecked) }
                            true
                        }
                        R.id.animation_item -> {
                            actionMenu?.findItem(menuItem.itemId)?.let { viewModel.changeAnimation(!it.isChecked) }
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun changeFullscreen(enabled: Boolean) {
        mainSharedViewModel.fullscreenMode(enabled)
        binding?.tiles?.keepScreenOn = enabled
    }

    private companion object {
        private const val KEY_TAP_SOUND_CHECKED = "KEY_TAP_SOUND_CHECKED"
        private const val KEY_ANIMATION_CHECKED = "KEY_ANIMATION_CHECKED"
    }
}
