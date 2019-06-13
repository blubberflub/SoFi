package com.blub.sofi.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(text: String, long: Boolean = false) {
    if (isAdded) {
        val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast
            .makeText(context, text, duration)
            .show()
    }
}