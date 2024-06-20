package com.linky.common.safe_coroutine.dispatchers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val linkyDispatcher: LinkyDispatchers)

enum class LinkyDispatchers {
    Default,
    IO,
}
