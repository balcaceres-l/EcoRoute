package com.example.ecoroute.data.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("msg") val msg: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("error_description") val errorDescription: String?,
    val code: String?
) {
    fun getDetail(): String {
        return message ?: msg ?: errorDescription ?: "Error desconocido (Código: $code)"
    }
}
