package com.eji14.cattycat.config

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eji14.cattycat.ui.DialogUI
import com.eji14.cattycat.ui.NotificationColors
import com.eji14.cattycat.ui.components.ButtonConfig
import com.eji14.cattycat.ui.components.MyTextData

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
    var textTitle: MyTextData
        private set
    var textLabel: MyTextData
        private set
    var textContent: MyTextData
        private set

    lateinit var buttonConfig: ButtonConfig
        private set
    lateinit var outlinedButtonConfig: ButtonConfig
        private set
    lateinit var textButtonConfig: ButtonConfig
        private set
    lateinit var tonalButtonConfig: ButtonConfig
        private set

    lateinit var extendedColors: ExtendedColors
        private set
    lateinit var dialogUI: DialogUI
        private set
    lateinit var notificationColors: NotificationColors
        private set

    init {
        textTitle = MyTextData(
            style = styles.titleMedium,
            color = colors.onSurface
        )
        textLabel = MyTextData(
            style = styles.labelMedium,
            color = colors.onSurfaceVariant
        )
        textContent = MyTextData(
            style = styles.bodyMedium,
            color = colors.onSurface
        )

        initializeBasic()

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
        if (!::dialogUI.isInitialized) dialogUI = DialogUI(
            containerColor = colors.surfaceContainerLow,
            titleColor = colors.onSurface,
            textColor = colors.onSurface,
            iconTint = colors.onSurface,
            titleTextData = textTitle,
            descriptionTextData = textContent,
            buttonTextData = textContent
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

    @Composable
    fun initializeWithCompose() {
        buttonConfig = ButtonConfig(
            shape = shapes.medium,
            colors = ButtonColors(
                containerColor = colors.primary,
                disabledContentColor = colors.primary.copy(alpha = 0.6f),
                contentColor = colors.onPrimary,
                disabledContainerColor = colors.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(),
            border = null,
            contentPadding = ButtonDefaults.ContentPadding,
            centered = true
        )

        tonalButtonConfig = ButtonConfig(
            shape = shapes.medium,
            colors = ButtonColors(
                containerColor = colors.primaryContainer,
                disabledContentColor = colors.primaryContainer.copy(alpha = 0.8f),
                contentColor = colors.onPrimaryContainer,
                disabledContainerColor = colors.onPrimaryContainer
            ),
            elevation = ButtonDefaults.buttonElevation(),
            border = null,
            contentPadding = ButtonDefaults.ContentPadding,
            centered = true
        )

        outlinedButtonConfig = ButtonConfig(
            shape = shapes.medium,
            colors = ButtonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                contentColor = colors.primary,
                disabledContentColor = colors.primary.copy(0.8f)
            ),
            elevation = null,
            border = BorderStroke(1.dp, colors.primary),
            contentPadding = ButtonDefaults.ContentPadding,
            centered = true
        )

        textButtonConfig = ButtonConfig(
            shape = shapes.medium,
            colors = ButtonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                contentColor = colors.primary,
                disabledContentColor = colors.primary.copy(0.8f)
            ),
            elevation = null,
            border = null,
            contentPadding = ButtonDefaults.ContentPadding,
            centered = true
        )

        initializeCompose()
    }

    protected abstract fun initializeBasic()
    @Composable protected abstract fun initializeCompose()
}

val LocalAppConfigBase = staticCompositionLocalOf<AppConfigBase> {
    error("No AppConfig provided")
}