package com.linky.timeline.state

sealed interface Sort {

    val text: String

    data object All : Sort {
        override val text: String = "전체"
    }

    data object Read : Sort {
        override val text: String = "읽음"
    }

    data object NoRead : Sort {
        override val text: String = "안읽음"
    }

}