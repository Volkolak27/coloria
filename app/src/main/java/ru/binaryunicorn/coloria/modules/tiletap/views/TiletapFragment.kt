package ru.binaryunicorn.coloria.modules.tiletap.views

import android.media.SoundPool
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.databinding.TiletapFragmentBinding
import ru.binaryunicorn.coloria.modules.main.SharedMainViewModel
import ru.binaryunicorn.coloria.modules.settings.SharedSettingsViewModel
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapView
import ru.binaryunicorn.coloria.modules.tiletap.presenters.TiletapPresenter
import javax.inject.Inject

class TiletapFragment : Fragment(), ITiletapView
{
    @Inject lateinit var presenter: TiletapPresenter
    override var initializated = false
    private var _binding: TiletapFragmentBinding? = null
    private val _sharedMainViewModel: SharedMainViewModel by activityViewModels()
    private val _sharedSettingsViewModel: SharedSettingsViewModel by activityViewModels()

    private var _actionMenu: Menu? = null

    private var _tapSoundChecked = false
    private var _animationChecked = false

    //// IMvpView ////

    override fun viewSelfSetup()
    {
        _sharedSettingsViewModel.settingsChanged().observe(this,
            Observer<String>
            {
                if (initializated)
                {
                    presenter.refreshTiletapFieldAction()
                }
            }
        )
    }

    //// Fragment ////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = TiletapFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        App.appComponent.inject(this)

        presenter.attachView(this)
        presenter.viewIsReady()
        return _binding?.root
    }

    override fun onResume()
    {
        super.onResume()

        val soundPool = SoundPool.Builder().also { it.setMaxStreams(10) }.build()
        val sounds = intArrayOf(
            soundPool.load(context, R.raw.tap01, 1),
            soundPool.load(context, R.raw.tap02, 1),
            soundPool.load(context, R.raw.tap03, 1)
        )

        _binding?.tiletapField?.updateSounds(soundPool, sounds)
        initializated = true
    }

    override fun onPause()
    {
        super.onPause()
        _binding?.tiletapField?.releaseSounds()
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        presenter.destroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_tiletaps, menu)

        _actionMenu = menu.apply {
            findItem(R.id.action_tap_sound).isChecked = _tapSoundChecked
            findItem(R.id.action_animation).isChecked = _animationChecked
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_1x1 -> presenter.fieldSizeChanged(1, 1)
            R.id.action_2x2 -> presenter.fieldSizeChanged(2, 2)
            R.id.action_4x4 -> presenter.fieldSizeChanged(4, 4)
            R.id.action_rgb -> presenter.rgbTestAction()
            R.id.action_settings -> presenter.toSettingsAction()
            R.id.action_full_screen -> _actionMenu?.findItem(item.itemId)?.let { presenter.fullscreenModeChanged(true) }
            R.id.action_tap_sound -> _actionMenu?.findItem(item.itemId)?.let { presenter.tapSoundChanged(!it.isChecked) }
            R.id.action_animation -> _actionMenu?.findItem(item.itemId)?.let { presenter.animationChanged(!it.isChecked) }
        }

        return super.onOptionsItemSelected(item)
    }

    //// ITiletapView ////

    override fun updateFieldSize(horizontalCount: Int, verticalCount: Int)
    {
        try
        {
            _binding?.tiletapField?.disableRgbTest()
            _binding?.tiletapField?.generateField(horizontalCount, verticalCount)
        }
        catch (e: OutOfMemoryError)
        {
            _sharedMainViewModel.toastMessage(getString(R.string.field_size_error))
            presenter.resetTiletapField()
        }
    }

    override fun updateTileWithRandomColor(horizontalPos: Int, verticalPos: Int)
    {
        _binding?.tiletapField?.setRandomColorForTile(horizontalPos, verticalPos)
    }

    override fun updateFullscreenMode(enabled: Boolean)
    {
        _binding?.tiletapField?.keepScreenOn = enabled
        _sharedMainViewModel.fullscreenMode(enabled)
    }

    override fun updateTapSound(enabled: Boolean)
    {
        _tapSoundChecked = enabled
        _actionMenu?.findItem(R.id.action_tap_sound)?.isChecked = enabled
        _binding?.tiletapField?.soundEnabled(enabled)
    }

    override fun updateAnimation(enabled: Boolean)
    {
        _animationChecked = enabled
        _actionMenu?.findItem(R.id.action_animation)?.isChecked = enabled
    }

    override fun refreshTiletapField()
    {
        _binding?.tiletapField?.refresh()
    }

    override fun showFieldSizeError()
    {
        _sharedMainViewModel.toastMessage(getString(R.string.field_size_error))
    }

    override fun openRgbTest()
    {
        _binding?.tiletapField?.let {
            if (it.isRgbTest())
            {
                it.disableRgbTest()
            }
            else
            {
                it.enableRgbTest()
            }
        }
    }

    override fun openSettings()
    {
        findNavController().navigate(R.id.navToSettings)
    }
}