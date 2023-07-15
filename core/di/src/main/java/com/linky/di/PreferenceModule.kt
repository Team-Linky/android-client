package com.linky.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

class DataStoreProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    val dataStore: DataStore<Preferences> get() = context.preferencesDataStore
}

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun providesDataStore(dataStoreProvider: DataStoreProvider): DataStore<Preferences> =
        dataStoreProvider.dataStore

}