package com.example.ecoroute.data.remote.api

import com.example.ecoroute.data.remote.request.LoginRequest
import com.example.ecoroute.data.remote.request.ProfileRequest
import com.example.ecoroute.data.remote.request.RegisterRequest
import com.example.ecoroute.data.remote.response.AuthResponse
import com.example.ecoroute.data.remote.response.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/v1/signup")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/v1/token?grant_type=password")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("rest/v1/profiles")
    @Headers("Prefer: return=representation")
    suspend fun createProfile(
        @Header("Authorization") bearerToken: String,
        @Body profile: ProfileRequest
    ): Response<List<ProfileResponse>>

    @GET("rest/v1/profiles")
    suspend fun getProfile(
        @Header("Authorization") bearerToken: String,
        @Query("id") id: String, 
        @Query("select") select: String = "*"
    ): Response<List<ProfileResponse>>
}
