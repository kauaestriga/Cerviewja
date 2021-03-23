package com.example.cerviewja.utils

import android.text.TextUtils
import android.util.Patterns

class ValidateUtils {

    companion object {
        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }

        fun isValidPassword(pass1: String, pass2: String): Boolean {
            return pass1 == pass2;
        }
    }
}