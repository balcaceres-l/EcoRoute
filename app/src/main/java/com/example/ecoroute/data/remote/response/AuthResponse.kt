package com.example.ecoroute.data.remote.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("token_type") val tokenType: String?,
    @SerializedName("expires_in") val expiresIn: Long?,
    @SerializedName("refresh_token") val refreshToken: String?,
    val user: AuthUser?
)

data class AuthUser(
    val id: String,
    val email: String?
)
