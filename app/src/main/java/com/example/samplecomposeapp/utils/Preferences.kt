package com.example.samplecomposeapp.utils

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private var sharedPreferences: SharedPreferences? = null

    fun initSharedPreference(context: Context?) {
        sharedPreferences = context?.getSharedPreferences("MyPreference", Context.MODE_PRIVATE)
    }

    fun savePreference(isLoggedIn: Boolean) {
        sharedPreferences?.edit()?.apply {
            putBoolean("isLoggedIn", isLoggedIn)
            apply()
        }
    }

    fun isLoggedIn() = sharedPreferences?.getBoolean("isLoggedIn", false) ?: false
}