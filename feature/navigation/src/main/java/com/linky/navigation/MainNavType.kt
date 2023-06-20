package com.linky.navigation

sealed class MainNavType constructor(
    val title: Int,
    val activeIcon: Int,
    val inactiveIcon: Int,
    val route: String
) {

    object TimeLine : MainNavType(
        title = R.string.nav_timeline,
        activeIcon = R.drawable.icon_nav_timeline_active,
        inactiveIcon = R.drawable.icon_nav_timeline_inactive,
        route = "nav_timeline",
    )

    object Tag : MainNavType(
        title = R.string.nav_tag,
        activeIcon = R.drawable.icon_nav_tag_active,
        inactiveIcon = R.drawable.icon_nav_tag_inactive,
        route = "nav_tag",
    )

    object More : MainNavType(
        title = R.string.nav_more,
        activeIcon = R.drawable.icon_nav_more_active,
        inactiveIcon = R.drawable.icon_nav_more_inactive,
        route = "nav_more",
    )

}

val NavList = listOf(
    MainNavType.TimeLine,
    MainNavType.Tag,
    MainNavType.More,
)