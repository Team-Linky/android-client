package com.linky.process_lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.linky.common.safe_coroutine.builder.safeLaunch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcessLifecycleObserver @Inject constructor() {

    private val _processLifecycleEvent = MutableSharedFlow<ProcessLifecycle>()
    val processLifecycleEvent = _processLifecycleEvent.asSharedFlow()

    private val internalProcessLifecycleObserver = InternalProcessLifecycleObserver(
        onBackground = ::emitBackground,
        onForeground = ::emitForeground
    )

    val lifecycleObserver: LifecycleObserver get() = internalProcessLifecycleObserver

    private suspend fun emitBackground() {
        _processLifecycleEvent.emit(ProcessLifecycle.Background)
    }

    private suspend fun emitForeground() {
        _processLifecycleEvent.emit(ProcessLifecycle.Foreground)
    }

}

sealed interface ProcessLifecycle {
    data object Background : ProcessLifecycle
    data object Foreground : ProcessLifecycle
}

private class InternalProcessLifecycleObserver(
    private val onBackground: suspend () -> Unit,
    private val onForeground: suspend () -> Unit,
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        owner.lifecycleScope.safeLaunch { onForeground.invoke() }
    }

    override fun onStop(owner: LifecycleOwner) {
        owner.lifecycleScope.safeLaunch { onBackground.invoke() }
    }

}