package com.linky.navigation.more

sealed class MoreNavType constructor(
    val route: String
) {
    object Notification : MoreNavType("more_nav_notification")
    object Tip : MoreNavType("more_nav_tip")
    object Lock : MoreNavType("more_nav_lock")
    object Tag : MoreNavType("more_nav_tag")
    object Link : MoreNavType("more_nav_link")
}