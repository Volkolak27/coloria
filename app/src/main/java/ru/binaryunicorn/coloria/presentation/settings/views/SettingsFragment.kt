package ru.binaryunicorn.coloria.presentation.settings.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.INVALID_POSITION
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.common.enums.AnimationSpeed
import ru.binaryunicorn.coloria.common.enums.AnimationType
import ru.binaryunicorn.coloria.databinding.FragmentSettingsBinding
import ru.binaryunicorn.coloria.di.viewmodel.ViewModelFactory
import ru.binaryunicorn.coloria.presentation.settings.ISettingsSharedViewModel
import ru.binaryunicorn.coloria.presentation.settings.ISettingsViewModel
import ru.binaryunicorn.coloria.presentation.settings.viewmodels.SettingsSharedViewModel
import ru.binaryunicorn.coloria.presentation.settings.viewmodels.SettingsViewModel

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ISettingsViewModel
    private lateinit var sharedViewModel: ISettingsSharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
        sharedViewModel = ViewModelProvider(this, viewModelFactory)[SettingsSharedViewModel::class.java]

        initSetup()

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //// Private ////

    private fun initSetup() {
        createActionMenu()

        val context = requireActivity()
        binding?.animationTypeValue?.adapter = AnimationType.stringAdapter(context)
        binding?.animationSpeedValue?.adapter = AnimationSpeed.stringAdapter(context)

        binding?.horizontalValue?.doOnTextChanged { text, _, _, _ ->
            val value = text.toString()
            viewModel.horizontalTilesCount = if (value.isNotEmpty()) value.toInt() else 1
        }

        binding?.verticalValue?.doOnTextChanged { text, _, _, _ ->
            val value = text.toString()
            viewModel.verticalTilesCount = if (value.isNotEmpty()) value.toInt() else 1
        }

        binding?.soundCheckbox?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isSoundEnabled = isChecked
        }

        binding?.animationCheckbox?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isAnimationEnabled = isChecked
            checkAnimationBlockAvailability()
        }

        binding?.animationTypeValue?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.animationType =
                        AnimationType.fromOrdinal(binding?.animationTypeValue?.selectedItemPosition ?: INVALID_POSITION)
                }
            }

        binding?.animationSpeedValue?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.animationSpeed =
                        AnimationSpeed.fromOrdinal(binding?.animationSpeedValue?.selectedItemPosition ?: INVALID_POSITION)
                }
            }

        binding?.applyButton?.setOnClickListener { _ ->
            applySettings()
        }

        viewModel.apply {
            binding?.horizontalValue?.setText(horizontalTilesCount.toString())
            binding?.verticalValue?.setText(verticalTilesCount.toString())
            binding?.soundCheckbox?.isChecked = isSoundEnabled
            binding?.animationCheckbox?.isChecked = isAnimationEnabled
            binding?.animationTypeValue?.setSelection(animationType.ordinal)
            binding?.animationSpeedValue?.setSelection(animationSpeed.ordinal)

            checkAnimationBlockAvailability()
        }
    }

    private fun createActionMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.settings, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.apply_item -> {
                            applySettings()
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

    private fun checkAnimationBlockAvailability() {
        val isAvailable = binding?.animationCheckbox?.isChecked ?: false
        binding?.animationTypeValue?.isEnabled = isAvailable
        binding?.animationSpeedValue?.isEnabled = isAvailable
    }

    private fun applySettings() {
        viewModel.applySettings()
        sharedViewModel.onSettingsDidChangeAndNeedReload()

        findNavController().popBackStack()
    }
}
