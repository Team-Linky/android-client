package com.linky.navigation

sealed class MainNavType(
    val title: Int,
    val activeIcon: Int,
    val inactiveIcon: Int,
    val route: String
) {

    data object TimeLine : MainNavType(
        title = R.string.nav_timeline,
        activeIcon = R.drawable.icon_nav_timeline_active,
        inactiveIcon = R.drawable.icon_nav_timeline_inactive,
        route = "nav_timeline",
    )

    data object Tag : MainNavType(
        title = R.string.nav_tag,
        activeIcon = R.drawable.icon_nav_tag_active,
        inactiveIcon = R.drawable.icon_nav_tag_inactive,
        route = "nav_tag",
    )

    data object More : MainNavType(
        title = R.string.nav_more,
        activeIcon = R.drawable.icon_nav_more_active,
        inactiveIcon = R.drawable.icon_nav_more_inactive,
        route = "nav_more",
    )

    companion object {
        fun containsRoute(route: String): Boolean {
            return arrayOf(TimeLine.route, Tag.route, More.route).contains(route)
        }
    }

}

val NavList = listOf(
    MainNavType.TimeLine,
    MainNavType.Tag,
    MainNavType.More,
)