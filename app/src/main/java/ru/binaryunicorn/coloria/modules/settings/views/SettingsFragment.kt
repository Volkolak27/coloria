package ru.binaryunicorn.coloria.modules.settings.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.enums.AnimationSpeed
import ru.binaryunicorn.coloria.enums.AnimationType
import ru.binaryunicorn.coloria.modules.ModuleStorage
import ru.binaryunicorn.coloria.modules.settings.ISettingsPresenter
import ru.binaryunicorn.coloria.modules.settings.ISettingsView
import ru.binaryunicorn.coloria.modules.settings.presenters.SettingsPresenter
import ru.binaryunicorn.coloria.modules.tiletap.presenters.TiletapPresenter
import ru.binaryunicorn.coloria.pattern.BasePresenter

class SettingsFragment : Fragment(), ISettingsView
{
    private var _presenter: ISettingsPresenter? = null

    private var _horizontalValue: EditText? = null
    private var _vertivalValue: EditText? = null
    private var _soundCheckBox: CheckBox? = null
    private var _animationCheckBox: CheckBox? = null
    private var _animationType: Spinner? = null
    private var _animationSpeed: Spinner? = null
    private var _confirmButton: Button? = null

    companion object
    {
        fun newInstance(): SettingsFragment
        {
            val args = Bundle()

            return SettingsFragment().also {
                it.arguments = args
            }
        }
    }

    fun inject(presenter: ISettingsPresenter)
    {
        _presenter = presenter
    }

    //// IMvpView ////

    override fun bind(view: View)
    {
        _horizontalValue = view.findViewById(R.id.horizontal_value)
        _vertivalValue = view.findViewById(R.id.vertical_value)
        _soundCheckBox = view.findViewById(R.id.sound_checkBox)
        _animationCheckBox = view.findViewById(R.id.animation_checkBox)
        _animationType = view.findViewById(R.id.animationType_value)
        _animationSpeed = view.findViewById(R.id.animationSpeed_value)
        _confirmButton = view.findViewById(R.id.confirm_button)
    }

    override fun setupUserInteractions()
    {
        _horizontalValue?.addTextChangedListener(
            object : TextWatcher
            {
                override fun afterTextChanged(s: Editable?)
                {
                    // empty
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
                {
                    // empty
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
                {
                    val string = s.toString()
                    if (string.isNotEmpty()) { _presenter?.horizontalCountChanged(string.toInt()) }
                }
            }
        )

        _vertivalValue?.addTextChangedListener(
            object : TextWatcher
            {
                override fun afterTextChanged(s: Editable?)
                {
                    // empty
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
                {
                    // empty
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
                {
                    val string = s.toString()
                    if (string.isNotEmpty()) { _presenter?.verticalCountChanged(string.toInt()) }
                }
            }
        )

        _soundCheckBox?.setOnCheckedChangeListener {
                _, isChecked ->
                _presenter?.tapSoundEnableChanged(isChecked)
        }

        _animationCheckBox?.setOnCheckedChangeListener {
                _, isChecked ->
                checkAvilibleOfAnimationBlock()
                _presenter?.animationEnableChanged(isChecked)
        }

        _animationType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {
                // empty
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                _presenter?.animationTypeChanged(AnimationType.fromId(position))
            }
        }

        _animationSpeed?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {
                // empty
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                _presenter?.animationSpeedChanged(AnimationSpeed.fromId(position))
            }
        }

        _confirmButton?.setOnClickListener {
            _ -> _presenter?.confirmAction()
        }
    }

    //// Fragment ////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val context = requireActivity()
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        setHasOptionsMenu(true)
        bind(view)

        if (savedInstanceState == null)
        {
            // empty
        }
        else
        {
            savedInstanceState.getString(BasePresenter.PRESENTER_GUID)?.let {
				_presenter = ModuleStorage.obtainPresenter(it) as SettingsPresenter
				_presenter?.attachView(this)
			}
        }

        _animationType?.adapter = AnimationType.stringAdapter(context)
        _animationSpeed?.adapter = AnimationSpeed.stringAdapter(context)

        _presenter?.viewIsReady()
        setupUserInteractions()
        return view
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState.putString(BasePresenter.PRESENTER_GUID, _presenter?.getGUID())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_settings, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_apply -> _presenter?.confirmAction()
        }

        return super.onOptionsItemSelected(item)
    }

    //// ISettingsView ////

    override fun updateHorizontalCount(count: Int)
    {
        _horizontalValue?.setText(count.toString())
    }

    override fun updateVerticalCount(count: Int)
    {
        _vertivalValue?.setText(count.toString())
    }

    override fun updateTapSoundEnable(enabled: Boolean)
    {
        _soundCheckBox?.isChecked = enabled
    }

    override fun updateAnimationEnable(enabled: Boolean)
    {
        _animationCheckBox?.isChecked = enabled
        checkAvilibleOfAnimationBlock()
    }

    override fun updateAnimationType(type: AnimationType)
    {
        _animationType?.setSelection(type.id())
    }

    override fun updateAnimationSpeed(speed: AnimationSpeed)
    {
        _animationSpeed?.setSelection(speed.id())
    }

    //// Private ////

    private fun checkAvilibleOfAnimationBlock()
    {
        val isAvailable = _animationCheckBox?.isChecked ?: false
        _animationType?.isEnabled = isAvailable
        _animationSpeed?.isEnabled = isAvailable
    }
}