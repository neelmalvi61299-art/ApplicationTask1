package com.networkmodule.wrapper

import com.example.core_module.manager.TokenManager
import com.networkmodule.retrofit.RetrofitClient


object NetworkController {

   private lateinit var retrofitClient: RetrofitClient
    private lateinit var baseUrl:String
    private lateinit var tokenManager : TokenManager

    fun init(baseUrl:String, tokenManager: TokenManager){
        this.baseUrl = baseUrl
        this.tokenManager = tokenManager
        this.retrofitClient = RetrofitClient(
            NetworkController.baseUrl
        )
    }

    fun getAPIClient(): RetrofitClient {
        return retrofitClient

    }

    fun getTokenManager():TokenManager{
        return tokenManager
    }
}