package com.example.ecoroute.repository

import com.example.ecoroute.data.remote.api.RetrofitClient
import com.example.ecoroute.data.remote.request.ProfileRequest
import com.example.ecoroute.data.remote.request.RegisterRequest
import com.example.ecoroute.data.remote.response.AuthResponse
import com.example.ecoroute.data.remote.response.ProfileResponse
import retrofit2.Response

class AuthRepository {
    private val apiService = RetrofitClient.instance

    suspend fun register(request: RegisterRequest): Response<AuthResponse> {
        return apiService.register(request)
    }

    suspend fun createProfile(token: String, profile: ProfileRequest): Response<List<ProfileResponse>> {
        val bearerToken = "Bearer $token"
        return apiService.createProfile(bearerToken, profile)
    }
}
