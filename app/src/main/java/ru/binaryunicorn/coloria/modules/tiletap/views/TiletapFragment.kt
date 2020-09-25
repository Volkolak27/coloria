package ru.binaryunicorn.coloria.modules.tiletap.views

import android.media.SoundPool
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.customviews.TileTapView
import ru.binaryunicorn.coloria.modules.ModuleStorage
import ru.binaryunicorn.coloria.modules.main.presenters.MainPresenter
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapPresenter
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapView
import ru.binaryunicorn.coloria.modules.tiletap.presenters.TiletapPresenter
import ru.binaryunicorn.coloria.pattern.BasePresenter

class TiletapFragment : Fragment(), ITiletapView
{
    private var _presenter: ITiletapPresenter? = null

    private var _fullScreenModeChecked: Boolean = false
    private var _tapSoundChecked: Boolean = false
    private var _animationChecked: Boolean = false

    private var _actionMenu: Menu? = null
    private var _tiletapField: TileTapView? = null

    companion object
    {
        fun newInstance(): TiletapFragment
        {
            val args = Bundle()

            return TiletapFragment().also {
                it.arguments = args
            }
        }
    }

    fun inject(presenter: ITiletapPresenter)
    {
        _presenter = presenter
    }

    //// IMvpView ////

    override fun bind(view: View)
    {
        _tiletapField = view.findViewById(R.id.tiletapField)
    }

    override fun setupUserInteractions()
    {
        // empty
    }

    //// Fragment ////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val context = requireActivity()
        val view = context.layoutInflater.inflate(R.layout.tiletap_fragment, container, false)
        setHasOptionsMenu(true)
        bind(view)

        if (savedInstanceState == null)
        {
            _presenter?.setupEasyLaunchFlag()
        }
        else
        {
            savedInstanceState.getString(BasePresenter.PRESENTER_GUID)?.let {
				_presenter = ModuleStorage.obtainPresenter(it) as TiletapPresenter
				_presenter?.attachView(this)
			}
        }

        _presenter?.viewIsReady()
        return view
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

        _tiletapField?.setSoundpoolAndSounds(soundPool, sounds)
    }

    override fun onPause()
    {
        super.onPause()
        _tiletapField?.releaseSoundpool()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        _presenter?.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState.putString(BasePresenter.PRESENTER_GUID, _presenter?.getGUID())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_tiletaps, menu)

        _actionMenu = menu.apply {
            findItem(R.id.action_full_screen).isChecked = _fullScreenModeChecked;
            findItem(R.id.action_tap_sound).isChecked = _tapSoundChecked;
            findItem(R.id.action_animation).isChecked = _animationChecked;
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_1x1 -> _presenter?.fieldSizeChanged(1, 1)
            R.id.action_2x2 -> _presenter?.fieldSizeChanged(2, 2)
            R.id.action_4x4 -> _presenter?.fieldSizeChanged(4, 4)
            R.id.action_settings -> _presenter?.routeToSettings()
            R.id.action_full_screen -> _actionMenu?.findItem(item.itemId)?.let { _presenter?.fullscreenModeChanged(!it.isChecked) }
            R.id.action_tap_sound -> _actionMenu?.findItem(item.itemId)?.let { _presenter?.tapSoundChanged(!it.isChecked) }
            R.id.action_animation -> _actionMenu?.findItem(item.itemId)?.let { _presenter?.animationChanged(!it.isChecked) }
        }

        return super.onOptionsItemSelected(item)
    }

    //// ITiletapView ////

    override fun updateFieldSize(horizontalCount: Int, verticalCount: Int)
    {
        _tiletapField?.generateField(horizontalCount, verticalCount)
    }

    override fun updateRandomColorForTile(horizontalPos: Int, verticalPos: Int)
    {
        _tiletapField?.setRandomColorForTile(horizontalPos, verticalPos)
    }

    override fun updateFullscreenMode(enabled: Boolean)
    {
        _fullScreenModeChecked = enabled
        _tiletapField?.keepScreenOn = enabled
        _actionMenu?.findItem(R.id.action_full_screen)?.isChecked = enabled
    }

    override fun updateTapSound(enabled: Boolean)
    {
        _tapSoundChecked = enabled
        _tiletapField?.tapSoundEnabled(enabled)
        _actionMenu?.findItem(R.id.action_tap_sound)?.isChecked = enabled
    }

    override fun updateAnimation(enabled: Boolean)
    {
        _animationChecked = enabled
        _actionMenu?.findItem(R.id.action_animation)?.isChecked = enabled
    }

    override fun refreshTiletapField()
    {
        _tiletapField?.refresh()
    }
}