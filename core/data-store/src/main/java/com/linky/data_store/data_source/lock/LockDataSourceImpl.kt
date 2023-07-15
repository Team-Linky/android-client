package com.linky.data_store.data_source.lock

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LockDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LockDataSource {

    companion object {
        private val PREFERENCE_KEY_ENABLE_LOCK = booleanPreferencesKey("preference_enable_lock")
        private val PREFERENCE_KEY_ENABLE_BIOMETRIC = booleanPreferencesKey("preference_enable_biometric")
    }

    override suspend fun enableLock(enable: Boolean) {
        dataStore.edit { it[PREFERENCE_KEY_ENABLE_LOCK] = enable }
    }

    override fun isEnabled(): Flow<Boolean> =
        dataStore.data.map { it[PREFERENCE_KEY_ENABLE_LOCK] ?: false }

    override suspend fun enableBiometric(enable: Boolean) {
        dataStore.edit { it[PREFERENCE_KEY_ENABLE_BIOMETRIC] = enable }
    }

    override fun isEnabledBiometric(): Flow<Boolean> =
        dataStore.data.map { it[PREFERENCE_KEY_ENABLE_BIOMETRIC] ?: false }

}