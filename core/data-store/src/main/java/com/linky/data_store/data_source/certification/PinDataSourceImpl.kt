package com.linky.data_store.data_source.certification

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CertificationDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : CertificationDataSource {

    companion object {
        private val PREFERENCE_KEY_PASSWORD = stringPreferencesKey("preference_password")
    }

    override suspend fun setPassword(password: String) {
        dataStore.edit { it[PREFERENCE_KEY_PASSWORD] = password }
    }

    override fun getPassword(): Flow<String?> = dataStore.data.map { it[PREFERENCE_KEY_PASSWORD] }

    override fun existCertification(): Flow<Boolean> = dataStore.data.map { it[PREFERENCE_KEY_PASSWORD] != null }

    override fun certified(password: String): Flow<Boolean> = dataStore.data.map { it[PREFERENCE_KEY_PASSWORD] == password }

}