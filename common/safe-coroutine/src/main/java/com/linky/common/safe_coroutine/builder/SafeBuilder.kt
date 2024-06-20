package com.linky.common.safe_coroutine.builder

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    ceh: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() },
    block: suspend CoroutineScope.() -> Unit
): Job = launch(
    context = context + ceh,
    start = start,
    block = block,
)

fun <T> CoroutineScope.safeAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    ceh: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() },
    block: suspend CoroutineScope.() -> T
): Deferred<T> = async(
    context = context + ceh,
    start = start,
    block = block
)