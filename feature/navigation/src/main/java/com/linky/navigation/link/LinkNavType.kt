package com.linky.navigation.link

sealed class LinkNavType constructor(
    val route: String
) {
    object URLInput : LinkNavType("link_nav_url_input")
    object DetailInput : LinkNavType("link_nav_detail_input/{url}")
    object Complete : LinkNavType("link_nav_complete")
}