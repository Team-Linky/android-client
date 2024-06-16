package com.linky.timeline.state

data class TimeLineState(
    val a: String? = null
) {
    companion object {
        val Init: TimeLineState
            get() = TimeLineState()
    }
}