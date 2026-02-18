package com.networkmodule.wrapper

import android.content.Context
import com.example.core_module.manager.TokenManager
import com.facebook.stetho.Stetho


class NetworkInitializer {

    private lateinit var baseUrl: String
    private lateinit var context: Context


    /*
    Initialization of retrofit object with application context and
    base url of restful apis
    * */
    fun initialize(context: Context, baseUrl: String, tokenManager: TokenManager) {
        this.baseUrl = baseUrl
        this.context = context
        if (com.example.network_module.BuildConfig.NETWORK_DEBUG) {
            Stetho.initializeWithDefaults(context)
        }
        NetworkController.init(baseUrl, tokenManager)
    }

    fun context(): Context {
        return context
    }

}