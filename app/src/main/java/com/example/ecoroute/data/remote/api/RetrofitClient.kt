package com.example.ecoroute.data.remote.api

import android.util.Log
import com.example.ecoroute.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    init {
        Log.d("EcoRouteNetwork", "Conexión con el servidor configurada exitosamente: ${BuildConfig.SUPABASE_URL}")
    }

    private val supabaseHeadersInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("apikey", BuildConfig.SUPABASE_ANON_KEY)
            .header("Content-Type", "application/json")

        if (originalRequest.header("Authorization").isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer ${BuildConfig.SUPABASE_ANON_KEY}")
        }

        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(supabaseHeadersInterceptor)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
