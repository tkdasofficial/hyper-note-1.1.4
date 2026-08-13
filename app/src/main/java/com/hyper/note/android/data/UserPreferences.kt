package com.hyper.note.android.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val name = MutableStateFlow(prefs.getString("name", "John Doe") ?: "John Doe")
    val dob = MutableStateFlow(prefs.getString("dob", "") ?: "")
    val address = MutableStateFlow(prefs.getString("address", "") ?: "")
    val encryptionKey = MutableStateFlow(prefs.getString("encryption_key", "0000") ?: "0000")
    val theme = MutableStateFlow(prefs.getString("theme", "Dark") ?: "Dark")
    val enableBiometrics = MutableStateFlow(prefs.getBoolean("enable_biometrics", false))
    val autoLockTimeout = MutableStateFlow(prefs.getString("auto_lock_timeout", "1 Minute") ?: "1 Minute")
    val fontSize = MutableStateFlow(prefs.getString("font_size", "Medium") ?: "Medium")
    val analyticsEnabled = MutableStateFlow(prefs.getBoolean("analytics_enabled", false))

    fun saveName(newName: String) {
        prefs.edit { putString("name", newName) }
        name.value = newName
    }
    
    fun saveDob(newDob: String) {
        prefs.edit { putString("dob", newDob) }
        dob.value = newDob
    }
    
    fun saveAddress(newAddress: String) {
        prefs.edit { putString("address", newAddress) }
        address.value = newAddress
    }
    
    fun saveEncryptionKey(newKey: String) {
        prefs.edit { putString("encryption_key", newKey) }
        encryptionKey.value = newKey
    }
    
    fun saveTheme(newTheme: String) {
        prefs.edit { putString("theme", newTheme) }
        theme.value = newTheme
    }

    fun saveEnableBiometrics(enabled: Boolean) {
        prefs.edit { putBoolean("enable_biometrics", enabled) }
        enableBiometrics.value = enabled
    }

    fun saveAutoLockTimeout(timeout: String) {
        prefs.edit { putString("auto_lock_timeout", timeout) }
        autoLockTimeout.value = timeout
    }

    fun saveFontSize(size: String) {
        prefs.edit { putString("font_size", size) }
        fontSize.value = size
    }

    fun saveAnalyticsEnabled(enabled: Boolean) {
        prefs.edit { putBoolean("analytics_enabled", enabled) }
        analyticsEnabled.value = enabled
    }
}
