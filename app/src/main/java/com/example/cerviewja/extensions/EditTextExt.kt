package com.example.cerviewja.extensions

import android.widget.EditText

fun EditText.getString(): String {
    return this.text.toString()
}

fun EditText.getDouble(): Double {
    return this.text.toString().toDouble()
}