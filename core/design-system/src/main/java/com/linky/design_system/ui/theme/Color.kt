package com.linky.design_system.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.Random

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

val RandomColor: Color
    get() = Color(
        red = Random().nextFloat() * (0.9f - 0.6f) + 0.6f,
        green = Random().nextFloat() * (0.9f - 0.6f) + 0.6f,
        blue = Random().nextFloat() * (0.9f - 0.6f) + 0.6f
    )

private val ColorFamilyWhiteAndGray999 = ColorFamily(White, Gray999)
private val ColorFamilyWhiteAndGray900 = ColorFamily(White, Gray900)
private val ColorFamilyGray800AndGray300 = ColorFamily(Gray800, Gray300)
private val ColorFamilyGray900AndGray100 = ColorFamily(Gray900, Gray100)
private val ColorFamilyGray600AndGray400 = ColorFamily(Gray600, Gray400)
private val ColorFamilyGray400AndGray600 = ColorFamily(Gray400, Gray600)
private val ColorFamilyGray100AndGray800 = ColorFamily(Gray100, Gray800)
private val ColorFamilyGray100AndGray900 = ColorFamily(Gray100, Gray900)
private val ColorFamilyGray300AndGray800 = ColorFamily(Gray300, Gray800)
private val ColorFamilyGray400AndGray800 = ColorFamily(Gray400, Gray800)
private val ColorFamilyNav300AndNav700 = ColorFamily(Nav300, Nav700)
private val ColorFamilyNav700AndNav300 = ColorFamily(Nav700, Nav300)
private val ColorFamilyGray100AndNav900 = ColorFamily(Gray100, Nav900)
private val ColorFamilyGray800AndGray400 = ColorFamily(Gray800, Gray400)
private val ColorFamilyGray100AndGray999 = ColorFamily(Gray100, Gray999)

val LinkyDefaultBackgroundColor
    @Composable get() = ColorFamilyWhiteAndGray999.color
val LinkyTextDefaultColor
    @Composable get() = ColorFamilyGray900AndGray100.color
val LinkyDescriptionColor
    @Composable get() = ColorFamilyGray800AndGray300.color

val NavigationUnSelectTextColor
    @Composable get() = ColorFamilyGray600AndGray400.color
val LinkyBaseTextFieldCursorColor
    @Composable get() = ColorFamilyGray600AndGray400.color
val LinkyButtonDisableColor
    @Composable get() = ColorFamilyGray400AndGray600.color
val LinkInputCompleteTextButtonDisableColor
    @Composable get() = ColorFamilyGray400AndGray600.color
val LinkyMoreContentLineColor
    @Composable get() = ColorFamilyGray100AndGray800.color
val LinkySwitchUnCheckTrackColor
    @Composable get() = ColorFamilyGray400AndGray800.color
val LinkyTimelineTextLineColor
    @Composable get() = ColorFamilyGray600AndGray400.color

val ClipBoardPasteBackgroundColor
    @Composable get() = ColorFamilyNav300AndNav700.color
val ClipBoardPasteTextColor
    @Composable get() = ColorFamilyNav700AndNav300.color
val WebContentBackgroundColor
    @Composable get() = ColorFamilyGray100AndGray900.color
val WebContentLineColor
    @Composable get() = ColorFamilyGray300AndGray800.color
val WebContentTitleColor
    @Composable get() = ColorFamilyGray600AndGray400.color
val LinkyTipBackgroundColor
    @Composable get() = ColorFamilyGray100AndNav900.color
val LockContentLineColor
    @Composable get() = ColorFamilyGray300AndGray800.color
val LockContentBackgroundColor
    @Composable get() = ColorFamilyGray100AndGray900.color
val CertificationDescriptionColor
    @Composable get() = ColorFamilyGray800AndGray300.color
val LinkyInputChipBackgroundColor
    @Composable get() = ColorFamilyGray800AndGray400.color
val LinkySortTextColor
    @Composable get() = ColorFamilyGray800AndGray400.color
val LinkyInputChipUnSelectTextColor
    @Composable get() = ColorFamilyGray800AndGray300.color
val LinkyInputChipSelectTextColor
    @Composable get() = ColorFamilyGray100AndGray999.color
val TimelineMenuBackgroundColor
    @Composable get() = ColorFamilyWhiteAndGray900.color

val MainColor
    @Composable get() = ColorFamily(MainColorLight, MainColorDark).color

val SubColor
    @Composable get() = ColorFamily(SubColorLight, SubColorDark).color