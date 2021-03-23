package com.example.cerviewja.utils

import android.app.AlertDialog
import android.content.Context
import com.example.cerviewja.R

class CustomDialog(
    var context: Context
) {

    fun showError(message: String?) {
        val errorDialog = AlertDialog.Builder(context)
        errorDialog.setTitle(R.string.ops)
        errorDialog.setMessage(message)
        errorDialog.setNeutralButton(R.string.got_it_error) { dialog, _ -> dialog.dismiss() }
        errorDialog.show()
    }

    fun showError(messageId: Int) {
        showError(context.getString(messageId))
    }

    fun showMessage(message: String?) {
        val errorDialog = AlertDialog.Builder(context)
        errorDialog.setTitle(R.string.iupi)
        errorDialog.setMessage(message)
        errorDialog.setPositiveButton(R.string.got_it_happy) { dialog, _ -> dialog.dismiss() }
        errorDialog.show()
    }

    fun showMessage(messageId: Int) {
        showMessage(context.getString(messageId))
    }
}