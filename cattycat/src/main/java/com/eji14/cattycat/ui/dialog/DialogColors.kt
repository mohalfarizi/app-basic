package com.eji14.cattycat.ui.dialog

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

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
    fun lightDialogColors() = mapOf(
        DialogType.ERROR to DialogColors(
            backgroundColor = Color(0xFFFFFFFF),
            iconColor = Color(0xFFF34F4F),
            iconBackgroundColor = Color(0xFFFFEBEE),
            titleColor = Color(0xFF212121),
            descriptionColor = Color(0xFF757575),
            primaryButtonColor = Color(0xFFF34F4F),
            primaryButtonTextColor = Color(0xFFFFFFFF),
            secondaryButtonBorderColor = Color(0xFFE0E0E0),
            secondaryButtonTextColor = Color(0xFF757575)
        ),
        DialogType.WARNING to DialogColors(
            backgroundColor = Color(0xFFFFFFFF),
            iconColor = Color(0xFFF57C00),
            iconBackgroundColor = Color(0xFFFFF3E0),
            titleColor = Color(0xFF212121),
            descriptionColor = Color(0xFF757575),
            primaryButtonColor = Color(0xFFF34F4F),
            primaryButtonTextColor = Color(0xFFFFFFFF),
            secondaryButtonBorderColor = Color(0xFFE0E0E0),
            secondaryButtonTextColor = Color(0xFF757575)
        ),
        DialogType.SUCCESS to DialogColors(
            backgroundColor = Color(0xFFFFFFFF),
            iconColor = Color(0xFF4CAF50),
            iconBackgroundColor = Color(0xFFE8F5E9),
            titleColor = Color(0xFF212121),
            descriptionColor = Color(0xFF757575),
            primaryButtonColor = Color(0xFF4CAF50),
            primaryButtonTextColor = Color(0xFFFFFFFF),
            secondaryButtonBorderColor = Color(0xFFE0E0E0),
            secondaryButtonTextColor = Color(0xFF757575)
        ),
        DialogType.INFO to DialogColors(
            backgroundColor = Color(0xFFFFFFFF),
            iconColor = Color(0xFF1976D2),
            iconBackgroundColor = Color(0xFFE3F2FD),
            titleColor = Color(0xFF212121),
            descriptionColor = Color(0xFF757575),
            primaryButtonColor = Color(0xFF1976D2),
            primaryButtonTextColor = Color(0xFFFFFFFF),
            secondaryButtonBorderColor = Color(0xFFE0E0E0),
            secondaryButtonTextColor = Color(0xFF757575)
        )
    )
    
    fun darkDialogColors() = mapOf(
        DialogType.ERROR to DialogColors(
            backgroundColor = Color(0xFF2C2C2C),
            iconColor = Color(0xFFF34F4F),
            iconBackgroundColor = Color(0xFF4A2121),
            titleColor = Color(0xFFFFFFFF),
            descriptionColor = Color(0xFFB0B0B0),
            primaryButtonColor = Color(0xFFF34F4F),
            primaryButtonTextColor = Color(0xFFFFFFFF),
            secondaryButtonBorderColor = Color(0xFF404040),
            secondaryButtonTextColor = Color(0xFFB0B0B0)
        ),
        DialogType.WARNING to DialogColors(
            backgroundColor = Color(0xFF2C2C2C),
            iconColor = Color(0xFFF57C00),
            iconBackgroundColor = Color(0xFF4A3821),
            titleColor = Color(0xFFFFFFFF),
            descriptionColor = Color(0xFFB0B0B0),
            primaryButtonColor = Color(0xFFF34F4F),
            primaryButtonTextColor = Color(0xFFFFFFFF),
            secondaryButtonBorderColor = Color(0xFF404040),
            secondaryButtonTextColor = Color(0xFFB0B0B0)
        ),
        DialogType.SUCCESS to DialogColors(
            backgroundColor = Color(0xFF2C2C2C),
            iconColor = Color(0xFF4CAF50),
            iconBackgroundColor = Color(0xFF1E3A1F),
            titleColor = Color(0xFFFFFFFF),
            descriptionColor = Color(0xFFB0B0B0),
            primaryButtonColor = Color(0xFF4CAF50),
            primaryButtonTextColor = Color(0xFFFFFFFF),
            secondaryButtonBorderColor = Color(0xFF404040),
            secondaryButtonTextColor = Color(0xFFB0B0B0)
        ),
        DialogType.INFO to DialogColors(
            backgroundColor = Color(0xFF2C2C2C),
            iconColor = Color(0xFF1976D2),
            iconBackgroundColor = Color(0xFF1A2F4A),
            titleColor = Color(0xFFFFFFFF),
            descriptionColor = Color(0xFFB0B0B0),
            primaryButtonColor = Color(0xFF1976D2),
            primaryButtonTextColor = Color(0xFFFFFFFF),
            secondaryButtonBorderColor = Color(0xFF404040),
            secondaryButtonTextColor = Color(0xFFB0B0B0)
        )
    )
}