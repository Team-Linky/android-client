package com.linky.design_system.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val MainColorLight = Color(0xFF4777EE)
val MainColorDark = Color(0xFF5B9DFF)

val SubColorLight = Color(0xFF2B85D8)
val SubColorDark = Color(0xFF6CF4DC)

val ErrorColor = Color(0xFFFF6E65)

val Gray999 = Color(0xFF101010)
val Gray900 = Color(0xFF1D1D22)
val Gray800 = Color(0xFF3B3B40)
val Gray600 = Color(0xFF909196)
val Gray400 = Color(0xFFCCCED3)
val Gray300 = Color(0xFFE7E8EB)
val Gray100 = Color(0xFFF5F6F8)
val White = Color(0xFFFFFFFF)

val Nav900 = Color(0xFF222B3F)
val Nav700 = Color(0xFF61728A)
val Nav500 = Color(0xFFC4CBDA)
val Nav300 = Color(0xFFECEFF4)

val ShadowGray = Color(0x33B5B5B5)
val ShadowBlue = Color(0x4D98ACD3)
val ShadowBlack = Color(0x99303030)

val ColorFamilyWhiteAndGray999
    @Composable get() = ColorFamily(White, Gray999).color
val ColorFamilyWhiteAndGray900
    @Composable get() = ColorFamily(White, Gray900).color
val ColorFamilyGray800AndGray300
    @Composable get() = ColorFamily(Gray800, Gray300).color
val ColorFamilyGray900AndGray100
    @Composable get() = ColorFamily(Gray900, Gray100).color
val ColorFamilyGray600AndGray400
    @Composable get() = ColorFamily(Gray600, Gray400).color
val ColorFamilyGray400AndGray600
    @Composable get() = ColorFamily(Gray400, Gray600).color
val ColorFamilyGray100AndGray800
    @Composable get() = ColorFamily(Gray100, Gray800).color
val ColorFamilyGray100AndGray900
    @Composable get() = ColorFamily(Gray100, Gray900).color
val ColorFamilyGray300AndGray800
    @Composable get() = ColorFamily(Gray300, Gray800).color
val ColorFamilyGray400AndGray800
    @Composable get() = ColorFamily(Gray400, Gray800).color
val ColorFamilyNav300AndNav700
    @Composable get() = ColorFamily(Nav300, Nav700).color
val ColorFamilyNav700AndNav300
    @Composable get() = ColorFamily(Nav700, Nav300).color
val ColorFamilyGray100AndNav900
    @Composable get() = ColorFamily(Gray100, Nav900).color
val ColorFamilyGray800AndGray400
    @Composable get() = ColorFamily(Gray800, Gray400).color
val ColorFamilyGray100AndGray999
    @Composable get() = ColorFamily(Gray100, Gray999).color
val ColorFamilyGray600AndGray800
    @Composable get() = ColorFamily(Gray600, Gray800).color
val ColorFamilyWhiteAndGray800
    @Composable get() = ColorFamily(White, Gray800).color

val MainColor
    @Composable get() = ColorFamily(MainColorLight, MainColorDark).color

val SubColor
    @Composable get() = ColorFamily(SubColorLight, SubColorDark).color