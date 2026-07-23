package com.example.ecoroute.data.remote.request

data class ProfileRequest(
    val id: String,
    val full_name: String,
    val role: String = "collector" // o "admin" según corresponda
)
