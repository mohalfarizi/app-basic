package com.eji14.cattycat.core.config

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import com.eji14.cattycat.ui.notification.NotificationColors
import com.eji14.cattycat.ui.theme.ExtendedColorScheme

@Suppress("unused")
@Immutable
abstract class AppConfigBase(
    internal val subColors: ColorScheme,
    internal val extendedColors: ExtendedColorScheme,
    internal val subStyles: Typography
) {
    private lateinit var notificationColors: NotificationColors

    @Composable
    fun InitializeWithCompose(colors: ColorScheme) {
        InitializeDefaults(colors)
        InitializeCompose()
    }

    @Composable
    protected abstract fun InitializeCompose()

    @Composable
    private fun InitializeDefaults(colors: ColorScheme) {
        val warning = extendedColors.warning
        val info = extendedColors.info
        val success = extendedColors.success

        notificationColors = NotificationColors(
            success = success.color,
            onSuccess = success.onColor,
            error = colors.error,
            onError = colors.onError,
            warning = warning.color,
            onWarning = warning.onColor,
            info = info.color,
            onInfo = info.onColor
        )
    }

    internal fun getNotificationColors() = notificationColors
}

val LocalAppConfigBase = staticCompositionLocalOf<AppConfigBase> {
    error("No AppConfig provided")
}