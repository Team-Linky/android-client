package com.linky.navigation.more

sealed class MoreNavType(val route: String) {
    data object Tip : MoreNavType("more_nav_tip")
    data object Lock : MoreNavType("more_nav_lock")
}