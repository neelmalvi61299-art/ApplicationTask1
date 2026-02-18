package com.networkmodule.retrofit


import com.facebook.stetho.okhttp3.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.networkmodule.interceptors.AuthenticationInterceptor
import com.networkmodule.interceptors.UrlValidationInterceptor
import com.networkmodule.wrapper.NetworkController
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(baseUrl: String) {

    private var defaultTimeOut: Long = 60


    private val apiLogLevel =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE

    private val retrofit by lazy {
        createRetrofitInstance(baseUrl)
    }

    private val apiMap: MutableMap<String, Any> = mutableMapOf()

    private fun createRetrofitInstance(baseUrl: String): Retrofit {
        val httpClientBuilder = getOkHttpClientBuilder()

        val retroBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClientBuilder.build())

//        getConverterFactory().forEach {
//            retroBuilder.addConverterFactory(it)
//        }
        return retroBuilder.build()
    }

    /**
     * Provide the OkHttpBuilder with all the basic logging. Override to change the default builder or add any property.
     * @return OkHttpBuilder to be passed in retrofit builder
     */
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = apiLogLevel
        var okHttpClient : OkHttpClient.Builder

        if(com.example.network_module.BuildConfig.NETWORK_DEBUG){
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(UrlValidationInterceptor())
                .addInterceptor(getAuthenticationInterceptor())
                .addNetworkInterceptor(StethoInterceptor())
                .addInterceptor(logging)
                .connectTimeout(defaultTimeOut, TimeUnit.SECONDS)
                .readTimeout(defaultTimeOut, TimeUnit.SECONDS)
                .writeTimeout(defaultTimeOut, TimeUnit.SECONDS)
        }else{
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(UrlValidationInterceptor())
                .addInterceptor(getAuthenticationInterceptor())
                .addInterceptor(logging)
                .connectTimeout(defaultTimeOut, TimeUnit.SECONDS)
                .readTimeout(defaultTimeOut, TimeUnit.SECONDS)
                .writeTimeout(defaultTimeOut, TimeUnit.SECONDS)
        }
        return okHttpClient
    }

    private fun getAuthenticationInterceptor(): AuthenticationInterceptor {
        return AuthenticationInterceptor(NetworkController.getTokenManager())
    }

//    /**
//     * Provide converter factory. And override to provide converter factory according to App requirements.
//     *
//     * @return Converter Factory
//     */
//    open fun getConverterFactory(): List<Converter.Factory> {
//        return listOf(nullOnEmptyConverterFactory, GsonConverterFactory.create(BaseApplication.gson))
//    }

    /**
     * Get the API Service instance for given class
     *
     * @param apiClass API interface class
     * @return api object reference to call Retrofit mode APIs
     */
    fun <T> getApiService(apiClass: Class<T>): Any? {
        var api: Any? = apiMap[apiClass.name]
        if (api == null) {
            api = retrofit.create(apiClass)
            apiMap[apiClass.name] = api!!
        }
        return api
    }

    /**
     * Add all the common headers which needs to be sent in API requests
     * Eg. - App Version, Source, device id, time zone..etc
     */
    private fun addCommonHeaders(builder: Request.Builder) {
        builder.addHeader("PLATFORM", "Android")
    }
}