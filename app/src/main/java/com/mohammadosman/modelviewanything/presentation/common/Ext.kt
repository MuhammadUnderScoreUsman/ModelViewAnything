package com.mohammadosman.modelviewanything.presentation.common

import android.app.Activity
import android.util.Log
import android.widget.Toast

const val TAG = "AppDebug"

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun logd(msg: String) {
    Log.d(TAG, msg)
}