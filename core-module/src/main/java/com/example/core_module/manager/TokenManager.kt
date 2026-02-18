package com.example.core_module.manager

import android.app.Application
import android.content.Context
import com.example.core_module.prefs.CoreSharedPref

open class TokenManager(private val context: Application) {

    private val tokenPrefs = TokenPrefs(context)
    private var _mutableToken: String?
    private var _refreshToken: String?
    val token: String?
        get() = _mutableToken

    val refreshtoken: String?
        get() = _refreshToken

    init {
        _mutableToken = tokenPrefs.prefs.getString(TOKEN_KEY, "")
        _refreshToken = tokenPrefs.prefs.getString(REFRESH_TOKEN, "")
    }


    fun isUserLoggedIn(): Boolean {
        return !token.isNullOrEmpty()
    }

    fun setAuthToken(token: String) {
        this._mutableToken = token
        tokenPrefs.editor.putString(TOKEN_KEY, token).apply()
    }

    fun setRefreshToken(refreshToken: String) {
        this._refreshToken = refreshToken
        tokenPrefs.editor.putString(REFRESH_TOKEN, refreshToken).apply()
    }

    fun reset() {
        setAuthToken("")
        setRefreshToken("")
//        (context as BaseApplication).onLogout()
    }

    inner class TokenPrefs(context: Context) : CoreSharedPref(context)

}


const val TOKEN_KEY = "token_key"
const val REFRESH_TOKEN = "refresh_token_key"
const val INTENT_USER_LOGGED_OUT = "intent_user_logged_out"