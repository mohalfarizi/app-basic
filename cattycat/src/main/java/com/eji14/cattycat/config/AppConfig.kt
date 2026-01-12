package com.eji14.cattycat.config

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val error: Color,
    val onError: Color,
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val outline: Color,
    val outlineVariant: Color
)

@Immutable
data class AppTextStyles(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle
)

@Immutable
data class AppSpacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp
)

@Immutable
data class AppElevation(
    val none: Dp = 0.dp,
    val small: Dp = 2.dp,
    val medium: Dp = 4.dp,
    val large: Dp = 8.dp
)

@Immutable
data class DialogColors(
    val containerColor: Color,
    val titleColor: Color,
    val textColor: Color,
    val iconTint: Color
)

@Immutable
data class AppConfig(
    val colors: AppColors,
    val textStyles: AppTextStyles,
    val spacing: AppSpacing = AppSpacing(),
    val elevation: AppElevation = AppElevation(),
    val buttonPadding: Dp = 16.dp,
    val textFieldColors: TextFieldColors,
    val dialogSuccessColors: DialogColors,
    val dialogErrorColors: DialogColors,
    val dialogWarningColors: DialogColors,
    val dialogInfoColors: DialogColors
) {
    companion object {
        @Composable
        fun default(): AppConfig {
            val colors = AppColors(
                primary = MaterialTheme.colorScheme.primary,
                onPrimary = MaterialTheme.colorScheme.onPrimary,
                secondary = MaterialTheme.colorScheme.secondary,
                onSecondary = MaterialTheme.colorScheme.onSecondary,
                background = MaterialTheme.colorScheme.background,
                onBackground = MaterialTheme.colorScheme.onBackground,
                surface = MaterialTheme.colorScheme.surface,
                onSurface = MaterialTheme.colorScheme.onSurface,
                surfaceContainerLowest = MaterialTheme.colorScheme.surfaceContainerLowest,
                surfaceContainerLow = MaterialTheme.colorScheme.surfaceContainerLow,
                surfaceContainer = MaterialTheme.colorScheme.surfaceContainer,
                surfaceContainerHigh = MaterialTheme.colorScheme.surfaceContainerHigh,
                error = MaterialTheme.colorScheme.error,
                onError = MaterialTheme.colorScheme.onError,
                success = Color(0xFF4CAF50),
                onSuccess = Color.White,
                warning = Color(0xFFFFA726),
                onWarning = Color.Black,
                outline = MaterialTheme.colorScheme.outline,
                outlineVariant = MaterialTheme.colorScheme.outlineVariant
            )

            val textStyles = AppTextStyles(
                displayLarge = MaterialTheme.typography.displayLarge,
                displayMedium = MaterialTheme.typography.displayMedium,
                displaySmall = MaterialTheme.typography.displaySmall,
                headlineLarge = MaterialTheme.typography.headlineLarge,
                headlineMedium = MaterialTheme.typography.headlineMedium,
                headlineSmall = MaterialTheme.typography.headlineSmall,
                titleLarge = MaterialTheme.typography.titleLarge,
                titleMedium = MaterialTheme.typography.titleMedium,
                titleSmall = MaterialTheme.typography.titleSmall,
                bodyLarge = MaterialTheme.typography.bodyLarge,
                bodyMedium = MaterialTheme.typography.bodyMedium,
                bodySmall = MaterialTheme.typography.bodySmall,
                labelLarge = MaterialTheme.typography.labelLarge,
                labelMedium = MaterialTheme.typography.labelMedium,
                labelSmall = MaterialTheme.typography.labelSmall
            )

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.outline,
                focusedLabelColor = colors.primary,
                unfocusedLabelColor = colors.onSurface.copy(alpha = 0.6f),
                cursorColor = colors.primary,
                errorBorderColor = colors.error,
                errorLabelColor = colors.error
            )

            return AppConfig(
                colors = colors,
                textStyles = textStyles,
                textFieldColors = textFieldColors,
                dialogSuccessColors = DialogColors(
                    containerColor = colors.success.copy(alpha = 0.1f),
                    titleColor = colors.success,
                    textColor = colors.onBackground,
                    iconTint = colors.success
                ),
                dialogErrorColors = DialogColors(
                    containerColor = colors.error.copy(alpha = 0.1f),
                    titleColor = colors.error,
                    textColor = colors.onBackground,
                    iconTint = colors.error
                ),
                dialogWarningColors = DialogColors(
                    containerColor = colors.warning.copy(alpha = 0.1f),
                    titleColor = colors.warning,
                    textColor = colors.onBackground,
                    iconTint = colors.warning
                ),
                dialogInfoColors = DialogColors(
                    containerColor = colors.primary.copy(alpha = 0.1f),
                    titleColor = colors.primary,
                    textColor = colors.onBackground,
                    iconTint = colors.primary
                )
            )
        }
    }
}

val LocalAppConfig = staticCompositionLocalOf<AppConfig> {
    error("No AppConfig provided")
}