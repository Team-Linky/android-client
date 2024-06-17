package com.linky.android

import androidx.lifecycle.ViewModel
import com.linky.data.usecase.lock.GetEnableLockUseCase
import com.linky.process_lifecycle.ProcessLifecycleObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getEnableLockUseCase: GetEnableLockUseCase,
    private val processLifecycleObserver: ProcessLifecycleObserver
) : ViewModel() {

    val processLifecycleEvent get() = processLifecycleObserver.processLifecycleEvent

    suspend fun getEnableLock(): Boolean = getEnableLockUseCase.state.first()

}