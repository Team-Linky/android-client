package com.linky.navigation

import android.os.Vibrator
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray999
import com.linky.design_system.ui.theme.NoRippleTheme
import com.linky.intercation.vibrate.vibrateCompat

@Composable
fun LinkyBottomNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        backgroundColor = ColorFamilyWhiteAndGray999,
    ) {
        BottomNavigation(
            modifier = Modifier.padding(end = 90.dp, bottom = 10.dp),
            elevation = 0.dp,
            backgroundColor = ColorFamilyWhiteAndGray999,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NoRippleTheme {
                NavList.forEach { navItem ->
                    val selected = currentRoute == navItem.route

                    BottomNavigationItem(
                        selected = selected,
                        selectedContentColor = ColorFamilyGray900AndGray100,
                        unselectedContentColor = ColorFamilyGray600AndGray400,
                        icon = {
                            val navIcon = if (selected) {
                                navItem.activeIcon
                            } else {
                                navItem.inactiveIcon
                            }
                            Image(
                                painter = painterResource(navIcon),
                                contentDescription = "navigation icon"
                            )
                        },
                        onClick = {
                            if (currentRoute != navItem.route) {
                                navController.navigate(route = navItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                vibrator.vibrateCompat()
                            }
                        },
                        label = {
                            val fontWeight = if (selected) {
                                FontWeight.Medium
                            } else {
                                FontWeight.SemiBold
                            }
                            LinkyText(
                                text = stringResource(navItem.title),
                                color = LocalContentColor.current,
                                fontWeight = fontWeight,
                                fontSize = 11.dp
                            )
                        }
                    )
                }
            }
        }
    }
}