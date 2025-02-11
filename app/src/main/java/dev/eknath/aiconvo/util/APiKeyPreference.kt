package dev.eknath.aiconvo.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


private object PreferenceKeys {
    const val prefName = "aiConvoPref"
    const val apiKeyPref = "_userApiKey"
    const val defaultValue = "default_value"
}


private fun Context.getMyPreference(): SharedPreferences? {
    return this.getSharedPreferences(PreferenceKeys.prefName, MODE_PRIVATE)
}

fun Context.getApiKeyOrDefault(): String? {
    val sharedPref = getMyPreference()
    val result = sharedPref?.getString(PreferenceKeys.apiKeyPref, PreferenceKeys.defaultValue)
    return if (result.contentEquals(PreferenceKeys.defaultValue)) null else result
}

fun Context.setApiKey(apiKey: String) {
    this.getMyPreference()?.edit()?.apply{ putString(PreferenceKeys.apiKeyPref, apiKey) }?.apply()
}