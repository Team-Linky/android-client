package com.linky.data.repository.pin

import com.linky.data_store.data_source.certification.PinDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PinRepositoryImpl @Inject constructor(
    private val pinDataSource: PinDataSource
) : PinRepository {

    override suspend fun setPin(password: String) =
        pinDataSource.setPin(password)

    override val pin: Flow<String?> = pinDataSource.pin

    override val existPin: Flow<Boolean> = pinDataSource.existPin

    override fun certified(pin: String): Flow<Boolean> = pinDataSource.certified(pin)

}