package com.team404.foodtrack.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.team404.foodtrack.R

object SnackbarBuilder {
    fun showErrorMessage(view: View) {
        Snackbar.make(view, R.string.error_message, Snackbar.LENGTH_SHORT).show()
    }
}