package com.linky.link_detail_input.state

sealed interface DetailInputSideEffect {
    data object TagTextClear : DetailInputSideEffect
}