package com.blub.sofi.utils

import android.util.Log

object Beaver {
    private const val DEBUG_TAG = "Beaver-DEBUG"
    private const val ERROR_TAG = "Beaver-ERROR"

    fun d(log: String) {
        Log.d(DEBUG_TAG, log)
    }

    fun e(message: String) {
        Log.e(ERROR_TAG, message)
    }
}