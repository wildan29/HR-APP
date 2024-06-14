package com.example.hrmaster.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login_session_pref")

@Singleton
class LoginSession @Inject constructor(@ApplicationContext private val context: Context) {

    val loginSessionFlow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preference ->
            preference[PreferencesKeys.SESSION_TOKEN] ?: ""
        }

    val roleFlow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preference ->
            preference[PreferencesKeys.ROLE] ?: ""
        }

    suspend fun updateLoginSession(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SESSION_TOKEN] = token
        }
    }

    suspend fun setRole(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ROLE] = name
        }
    }

    val errorAbsenFlow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preference ->
            preference[PreferencesKeys.ERROR_ABSEN_TODAY] ?: ""
        }

    suspend fun setErrorMessage(msg: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ERROR_ABSEN_TODAY] = msg
        }
    }

    suspend fun deleteErrorMessage() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.ERROR_ABSEN_TODAY)
        }
    }

    val nameSessionFLow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preference ->
            preference[PreferencesKeys.NAME_SESSION] ?: ""
        }

    suspend fun setName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME_SESSION] = name
        }
    }

    val profileSessionFLow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preference ->
            preference[PreferencesKeys.PROFILE_SESSION] ?: ""
        }

    suspend fun setProfile(profile: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROFILE_SESSION] = profile
        }
    }

    suspend fun deleteSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private object PreferencesKeys {
        val SESSION_TOKEN = stringPreferencesKey("session_token_pref")
        val ROLE = stringPreferencesKey("role_pref")
        val ERROR_ABSEN_TODAY = stringPreferencesKey("error_absen_today")
        val NAME_SESSION = stringPreferencesKey("nama_pref")
        val PROFILE_SESSION = stringPreferencesKey("profile_pref")
    }
}