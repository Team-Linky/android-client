package com.linky.navigation.link

sealed class LinkNavType(val route: String) {
    data object URLInput : LinkNavType("link_nav_url_input")
    data object DetailInput : LinkNavType("link_nav_detail_input/{url}")
    data object Complete : LinkNavType("link_nav_complete")
}