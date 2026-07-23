package com.example.ecoroute.data.local.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.authSessionDataStore by preferencesDataStore(name = "auth_session")

data class AuthSession(
    val accessToken: String?,
    val refreshToken: String?,
    val userId: String?,
    val email: String?
) {
    val isLoggedIn: Boolean
        get() = !accessToken.isNullOrBlank()
}

class SessionManager(private val context: Context) {

    val session: Flow<AuthSession> = context.authSessionDataStore.data.map { preferences ->
        AuthSession(
            accessToken = preferences[ACCESS_TOKEN],
            refreshToken = preferences[REFRESH_TOKEN],
            userId = preferences[USER_ID],
            email = preferences[EMAIL]
        )
    }

    suspend fun saveSession(
        accessToken: String,
        refreshToken: String?,
        userId: String?,
        email: String?
    ) {
        context.authSessionDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            refreshToken?.let { preferences[REFRESH_TOKEN] = it } ?: preferences.remove(REFRESH_TOKEN)
            userId?.let { preferences[USER_ID] = it } ?: preferences.remove(USER_ID)
            email?.let { preferences[EMAIL] = it } ?: preferences.remove(EMAIL)
        }
    }

    suspend fun clearSession() {
        context.authSessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_ID = stringPreferencesKey("user_id")
        val EMAIL = stringPreferencesKey("email")
    }
}
