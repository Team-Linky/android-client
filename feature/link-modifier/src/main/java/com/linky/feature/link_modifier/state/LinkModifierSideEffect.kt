package com.linky.feature.link_modifier.state

sealed interface LinkModifierSideEffect {
    data object TagTextClear : LinkModifierSideEffect
}