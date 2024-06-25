package com.linky.data_store.data_source.pin

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PinDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PinDataSource {

    companion object {
        private val PREFERENCE_KEY_PIN = stringPreferencesKey("preference_pin")
    }

    override suspend fun setPin(password: String) {
        dataStore.edit { it[PREFERENCE_KEY_PIN] = password }
    }

    override val pin: Flow<String?> = dataStore.data.map { it[PREFERENCE_KEY_PIN] }

    override val existPin: Flow<Boolean> = dataStore.data.map { it[PREFERENCE_KEY_PIN] != null }

    override fun certified(pin: String): Flow<Boolean> = dataStore.data.map { it[PREFERENCE_KEY_PIN] == pin }

}