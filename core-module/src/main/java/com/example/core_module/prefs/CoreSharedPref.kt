package com.example.core_module.prefs

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

open class CoreSharedPref(context: Context) {
    val prefs: SharedPreferences = context.getSharedPreferences(CoreSharedPref::class.java.simpleName, Activity.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = prefs.edit()
    open fun clear() {
        editor.clear().commit()
    }
    fun contains(key: String) = prefs.contains(key)

    fun register(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregister(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun saveValue(key:String,value: String){
        editor.putString(key, value).apply()
    }

    fun getValue(key: String):String{
        return prefs.getString(key,"")!!
    }

    var mobile: String?
        get() = prefs.getString("mobile", "")
        set(token) = editor.putString("mobile", token).apply()

    var playerName: String?
        get() = prefs.getString("player_name", "")
        set(token) = editor.putString("player_name", token).apply()

    var playerId: String?
        get() = prefs.getString("playerId", "")
        set(token) = editor.putString("playerId", token).apply()

    var walletId: String?
        get() = prefs.getString("walletId", "")
        set(token) = editor.putString("walletId", token).apply()

    var freshdeskRestoreId: String?
        get() = prefs.getString("freshDeskRestoreId", "")
        set(token) = editor.putString("freshDeskRestoreId", token).apply()

    var referalcode: String?
        get()=  prefs.getString("referalcode", "") ?: ""
        set(value) = editor.putString("referalcode", value).apply()

    var email: String?
        get()=  prefs.getString("email", "") ?: ""
        set(value) = editor.putString("email", value).apply()

    var userimage: String?
        get()=  prefs.getString("userimage", "") ?: ""
        set(value) = editor.putString("userimage", value).apply()

    var username: String?
        get()=  prefs.getString("username", "") ?: ""
        set(value) = editor.putString("username", value).apply()

    var contactsUploaded: Boolean?
        get() = prefs.getBoolean("contactsUploaded", false)
        set(token) = editor.putBoolean("contactsUploaded", token ?: false).apply()

    var contactPermissionDenied: Boolean?
        get() = prefs.getBoolean("contactPermissionDenied", false)
        set(token) = editor.putBoolean("contactPermissionDenied", token ?: false).apply()
}