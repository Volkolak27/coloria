package ru.binaryunicorn.coloria.presentation.about.views

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.binaryunicorn.coloria.BuildConfig
import ru.binaryunicorn.coloria.Consts
import ru.binaryunicorn.coloria.R
import ru.binaryunicorn.coloria.databinding.DialogAboutBinding

class AboutDialog : DialogFragment() {

    private lateinit var binding: DialogAboutBinding

    //// DialogFragment ////

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireActivity()
        binding = DialogAboutBinding.inflate(context.layoutInflater)

        binding.caption.text = context.getString(R.string.app_name)
        binding.version.text = BuildConfig.VERSION_NAME

        binding.rateButton.setOnClickListener {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Consts.GPLAY_ADDR)
                    )
                )
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return AlertDialog.Builder(context).also {
            it.setView(binding.root)
            it.setPositiveButton(resources.getString(R.string.ok)) { _, _ -> dismiss() }
        }.create()
    }

    companion object {
        fun newInstance(): AboutDialog {
            val args = Bundle()

            return AboutDialog().also {
                it.arguments = args
            }
        }
    }
}
