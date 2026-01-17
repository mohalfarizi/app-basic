package com.eji14.cattycat.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColorScheme(
    val warning: ColorFamily,
    val info: ColorFamily,
    val success: ColorFamily,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val extendedLight = ExtendedColorScheme(
    warning = ColorFamily(
        warningLight,
        onWarningLight,
        warningContainerLight,
        onWarningContainerLight,
    ),
    info = ColorFamily(
        infoLight,
        onInfoLight,
        infoContainerLight,
        onInfoContainerLight,
    ),
    success = ColorFamily(
        successLight,
        onSuccessLight,
        successContainerLight,
        onSuccessContainerLight,
    ),
)

val extendedDark = ExtendedColorScheme(
    warning = ColorFamily(
        warningDark,
        onWarningDark,
        warningContainerDark,
        onWarningContainerDark,
    ),
    info = ColorFamily(
        infoDark,
        onInfoDark,
        infoContainerDark,
        onInfoContainerDark,
    ),
    success = ColorFamily(
        successDark,
        onSuccessDark,
        successContainerDark,
        onSuccessContainerDark,
    ),
)