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
import com.eji14.cattycat.ui.components.ButtonConfig
import com.eji14.cattycat.ui.components.MyTextData
import com.eji14.cattycat.ui.dialog.DialogConfig
import com.eji14.cattycat.ui.theme.ExtendedColorScheme

@Immutable
abstract class AppConfigBase(
    val colors: ColorScheme,
    extendedColors: ExtendedColorScheme,
    val styles: Typography,
    val shapes: Shapes,
) {
    val warning = extendedColors.warning
    val info = extendedColors.info
    val success = extendedColors.success

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

    lateinit var dialogUI: DialogUI
        private set

    internal lateinit var defaultDialogConfig: DialogConfig
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

        if (!::dialogUI.isInitialized) dialogUI = DialogUI(
            containerColor = colors.surfaceContainerLow,
            titleColor = colors.onSurface,
            textColor = colors.onSurface,
            iconTint = colors.onSurface,
            titleTextData = textTitle,
            descriptionTextData = textContent,
            buttonTextData = textContent
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

        defaultDialogConfig = DialogConfig()
        initializeCompose()
    }

    protected abstract fun initializeBasic()
    @Composable protected abstract fun initializeCompose()
}

val LocalAppConfigBase = staticCompositionLocalOf<AppConfigBase> {
    error("No AppConfig provided")
}