package ru.binaryunicorn.coloria.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import ru.binaryunicorn.coloria.BuildConfig
import ru.binaryunicorn.coloria.R

class AboutDialog : DialogFragment()
{
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
        val view = context.layoutInflater.inflate(R.layout.about_dialog, null)

        // Bind
        view.findViewById<TextView>(R.id.caption).text = context.applicationInfo.nonLocalizedLabel.toString()
        view.findViewById<TextView>(R.id.version).text = BuildConfig.VERSION_NAME

        view.findViewById<Button>(R.id.rate_button).setOnClickListener {
            try
            {
                Intent(Intent.ACTION_VIEW).also {
                    it.data = Uri.parse("market://details?id=ru.binaryunicorn.coloria")
                    context.startActivity(it)
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }

        return AlertDialog.Builder(requireActivity()).also {
            it.setView(view)
            it.setPositiveButton(resources.getString(R.string.ok)) { _, _ -> dismiss() }
        }.create()
    }
}