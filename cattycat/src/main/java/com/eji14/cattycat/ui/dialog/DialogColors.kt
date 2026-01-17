package com.eji14.cattycat.ui.dialog

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.eji14.cattycat.ui.theme.ExtendedColorScheme

data class DialogColors(
    val backgroundColor: Color,
    val iconColor: Color,
    val iconBackgroundColor: Color,
    val titleColor: Color,
    val descriptionColor: Color,
    val primaryButtonColor: Color,
    val primaryButtonTextColor: Color,
    val secondaryButtonBorderColor: Color,
    val secondaryButtonTextColor: Color
)

val LocalDialogColors = compositionLocalOf<Map<DialogType, DialogColors>> {
    error("No DialogColors provided")
}

object DialogDefaults {
    @Composable
    fun dialogColors(colors: ColorScheme, extendedColors: ExtendedColorScheme): Map<DialogType, DialogColors> {
        val warning = extendedColors.warning
        val info = extendedColors.info
        val success = extendedColors.success

        return mapOf(
            DialogType.ERROR to DialogColors(
                backgroundColor = colors.onError,
                iconColor = colors.error,
                iconBackgroundColor = colors.errorContainer,
                titleColor = colors.error,
                descriptionColor = LocalContentColor.current,
                primaryButtonColor = colors.error,
                primaryButtonTextColor = colors.onError,
                secondaryButtonBorderColor = colors.outline,
                secondaryButtonTextColor = colors.error
            ),
            DialogType.WARNING to DialogColors(
                backgroundColor = warning.onColor,
                iconColor = warning.color,
                iconBackgroundColor = warning.colorContainer,
                titleColor = warning.color,
                descriptionColor = LocalContentColor.current,
                primaryButtonColor = warning.color,
                primaryButtonTextColor = warning.onColor,
                secondaryButtonBorderColor = colors.outline,
                secondaryButtonTextColor = warning.color
            ),
            DialogType.SUCCESS to DialogColors(
                backgroundColor = success.onColor,
                iconColor = success.color,
                iconBackgroundColor = success.colorContainer,
                titleColor = success.color,
                descriptionColor = LocalContentColor.current,
                primaryButtonColor = success.color,
                primaryButtonTextColor = success.onColor,
                secondaryButtonBorderColor = colors.outline,
                secondaryButtonTextColor = success.color
            ),
            DialogType.INFO to DialogColors(
                backgroundColor = info.onColor,
                iconColor = info.color,
                iconBackgroundColor = info.colorContainer,
                titleColor = info.color,
                descriptionColor = LocalContentColor.current,
                primaryButtonColor = info.color,
                primaryButtonTextColor = info.onColor,
                secondaryButtonBorderColor = colors.outline,
                secondaryButtonTextColor = info.color
            )
        )
    }
}