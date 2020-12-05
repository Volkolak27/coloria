package ru.binaryunicorn.coloria.modules.settings.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.databinding.SettingsFragmentBinding
import ru.binaryunicorn.coloria.extra.enums.AnimationSpeed
import ru.binaryunicorn.coloria.extra.enums.AnimationType
import ru.binaryunicorn.coloria.modules.settings.ISettingsPresenter
import ru.binaryunicorn.coloria.modules.settings.ISettingsView
import ru.binaryunicorn.coloria.modules.settings.SharedSettingsViewModel
import javax.inject.Inject

class SettingsFragment : Fragment(), ISettingsView
{
    @Inject lateinit var presenter: ISettingsPresenter
    override var initializated = false
    private var _binding: SettingsFragmentBinding? = null
	private val _sharedSettingsViewModel: SharedSettingsViewModel by activityViewModels()

    //// IMvpView ////

    override fun viewSelfSetup()
    {
        val context = requireActivity()
        _binding?.animationTypeValue?.adapter = AnimationType.stringAdapter(context)
        _binding?.animationSpeedValue?.adapter = AnimationSpeed.stringAdapter(context)

        _binding?.horizontalValue?.addTextChangedListener(
            object : TextWatcher
            {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
                {
                    val string = s.toString()
                    presenter.horizontalCountChanged( if (string.isNotEmpty()) string.toInt() else 0 )
                }
            }
        )

        _binding?.verticalValue?.addTextChangedListener(
            object : TextWatcher
            {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
                {
                    val string = s.toString()
                    presenter.verticalCountChanged( if (string.isNotEmpty()) string.toInt() else 0 )
                }
            }
        )

        _binding?.soundCheckBox?.setOnCheckedChangeListener {
            _, isChecked -> presenter.tapSoundEnableChanged(isChecked)
        }

        _binding?.animationCheckBox?.setOnCheckedChangeListener {
            _, isChecked ->
                checkAvilibleOfAnimationBlock()
                presenter.animationEnableChanged(isChecked)
        }

        _binding?.animationTypeValue?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener
            {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
                {
                    presenter.animationTypeChanged(AnimationType.fromId(position))
                }
            }

        _binding?.animationSpeedValue?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener
            {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
                {
                    presenter.animationSpeedChanged(AnimationSpeed.fromId(position))
                }
            }

        _binding?.confirmButton?.setOnClickListener {
            _ -> presenter.confirmAction()
        }
    }

    //// Fragment ////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        App.appComponent.inject(this)

        presenter.attachView(this)
        presenter.viewIsReady()
        return _binding?.root
    }

    override fun onResume()
    {
        super.onResume()
        initializated = true
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
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
            R.id.action_apply -> presenter.confirmAction()
        }

        return super.onOptionsItemSelected(item)
    }

    //// ISettingsView ////

    override fun updateHorizontalCount(count: Int)
    {
        _binding?.horizontalValue?.setText(count.toString())
    }

    override fun updateVerticalCount(count: Int)
    {
        _binding?.verticalValue?.setText(count.toString())
    }

    override fun updateTapSoundEnable(enabled: Boolean)
    {
        _binding?.soundCheckBox?.isChecked = enabled
    }

    override fun updateAnimationEnable(enabled: Boolean)
    {
        _binding?.animationCheckBox?.isChecked = enabled
        checkAvilibleOfAnimationBlock()
    }

    override fun updateAnimationType(type: AnimationType)
    {
        _binding?.animationTypeValue?.setSelection(type.id())
    }

    override fun updateAnimationSpeed(speed: AnimationSpeed)
    {
        _binding?.animationSpeedValue?.setSelection(speed.id())
    }

    override fun confirm()
    {
        _sharedSettingsViewModel.settingsDidChange()
        findNavController().popBackStack()
    }

    //// Private ////

    private fun checkAvilibleOfAnimationBlock()
    {
        val isAvailable = _binding?.animationCheckBox?.isChecked ?: false
        _binding?.animationTypeValue?.isEnabled = isAvailable
        _binding?.animationSpeedValue?.isEnabled = isAvailable
    }
}