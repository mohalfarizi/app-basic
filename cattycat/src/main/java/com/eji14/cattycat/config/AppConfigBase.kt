package com.eji14.cattycat.config

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.eji14.cattycat.ui.DialogColors
import com.eji14.cattycat.ui.NotificationColors

@Immutable
data class ExtendedColors(
    val success: Color,
    val successDisabled: Color,
    val onSuccess: Color,
    val error: Color,
    val errorDisabled: Color,
    val onError: Color,
    val warning: Color,
    val warningDisabled: Color,
    val onWarning: Color,
    val info: Color,
    val infoDisabled: Color,
    val onInfo: Color
)

@Immutable
abstract class AppConfigBase(
    val colors: ColorScheme,
    val styles: Typography,
    val shapes: Shapes,
) {
    lateinit var extendedColors: ExtendedColors
        private set
    lateinit var dialogColors: DialogColors
        private set
    lateinit var notificationColors: NotificationColors
        private set

    init {
        initialize()
        if (!::extendedColors.isInitialized) extendedColors = ExtendedColors(
            success = Color(0xFF4cae4f),
            successDisabled = Color(0xAA4cae4f),
            onSuccess = Color(0xFFFFFFFF),
            error = colors.error,
            errorDisabled = colors.error.copy(alpha = 0.38f),
            onError = colors.onError,
            warning = Color(0xFFff6700),
            warningDisabled = Color(0xAAff6700),
            onWarning = colors.onError,
            info = Color(0xFF00BDE7),
            infoDisabled = Color(0xAA00BDE7),
            onInfo = Color(0xFFFFFFFF)
        )
        if (!::dialogColors.isInitialized) dialogColors = DialogColors(
            containerColor = colors.surfaceContainerLow,
            titleColor = colors.onSurface,
            textColor = colors.onSurface,
            iconTint = colors.onSurface
        )

        if (!::notificationColors.isInitialized) notificationColors = NotificationColors(
            success = extendedColors.success,
            onSuccess = extendedColors.onSuccess,
            error = extendedColors.error,
            onError = extendedColors.onError,
            warning = extendedColors.warning,
            onWarning = extendedColors.onWarning,
            info = extendedColors.info,
            onInfo = extendedColors.onInfo
        )
    }

    protected abstract fun initialize()
    @Composable abstract fun initializeUI()
}

val LocalAppConfigBase = staticCompositionLocalOf<AppConfigBase> {
    error("No AppConfig provided")
}