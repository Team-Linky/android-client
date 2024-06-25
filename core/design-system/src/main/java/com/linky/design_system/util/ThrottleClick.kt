package com.linky.design_system.util

import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

private const val THROTTLE_CLICK_DURATION = 300L

interface SingleClickEventListener {
    fun onClick(action: () -> Unit)
}

@Composable
fun <T> throttleClick(
    duration: Long = THROTTLE_CLICK_DURATION,
    content: @Composable (SingleClickEventListener) -> T
): T {
    val debounceState = remember {
        MutableSharedFlow<() -> Unit>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }

    val result = content.invoke(
        object : SingleClickEventListener {
            override fun onClick(action: () -> Unit) {
                debounceState.tryEmit(action)
            }
        }
    )

    LaunchedEffect(Unit) {
        debounceState
            .throttleFirst(duration)
            .collect { it.invoke() }
    }

    return result
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.throttleClick(
    duration: Long = THROTTLE_CLICK_DURATION,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    throttleClick(duration = duration) { singleClickListener ->
        clickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            onClick = { singleClickListener.onClick { onClick.invoke() } },
            role = role,
            indication = LocalIndication.current,
            interactionSource = interactionSource ?: remember { MutableInteractionSource() }
        )
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.throttleClickRipple(
    duration: Long = THROTTLE_CLICK_DURATION,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    bounded: Boolean = true,
    enableRipple: Boolean = true,
    radius: Dp = Dp.Unspecified,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    throttleClick(duration = duration) { singleClickListener ->
        clickableRipple(
            bounded = bounded,
            enableRipple = enableRipple,
            radius = radius,
            onClick = { singleClickListener.onClick { onClick.invoke() } },
        )
    }
}

private fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime > windowDuration) {
            lastEmissionTime = currentTime
            emit(upstream)
        }
    }
}