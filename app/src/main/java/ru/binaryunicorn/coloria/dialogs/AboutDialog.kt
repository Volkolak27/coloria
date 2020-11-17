package ru.binaryunicorn.coloria.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.binaryunicorn.coloria.BuildConfig
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.databinding.AboutDialogBinding

class AboutDialog : DialogFragment()
{
    private var _binding: AboutDialogBinding? = null; private val binding get() = _binding!!

    companion object
    {
        fun newInstance(): AboutDialog
        {
            val args = Bundle()

            return AboutDialog().also {
                it.arguments = args
            }
        }
    }

    //// DialogFragment ////

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val context = requireActivity()
        _binding = AboutDialogBinding.inflate(context.layoutInflater)

        binding.caption.text = context.applicationInfo.nonLocalizedLabel.toString()
        binding.version.text = BuildConfig.VERSION_NAME

        binding.rateButton.setOnClickListener {
            try
            {
                context.startActivity( Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=ru.binaryunicorn.coloria")) )
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }

        return AlertDialog.Builder(context).also {
            it.setView(binding.root)
            it.setPositiveButton(resources.getString(R.string.ok)) { _, _ -> dismiss() }
        }.create()
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}