package com.eji14.appbasic.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.eji14.appbasic.AppConfig
import com.eji14.cattycat.config.LocalAppConfigBase
import com.eji14.cattycat.ui.theme.extendedDark
import com.eji14.cattycat.ui.theme.extendedLight

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun AppBasicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val extendedColors = when {
        darkTheme -> extendedDark
        else -> extendedLight
    }
    val typography = Typography.copy(
        bodyLarge = Typography.bodyLarge.copy(color = colorScheme.outline),
        bodyMedium = Typography.bodyMedium.copy(color = colorScheme.outline),
        bodySmall = Typography.bodySmall.copy(color = colorScheme.outline)
    )
    val shapes = Shapes(
        medium = RoundedCornerShape(1.dp),
        small = RoundedCornerShape(5.dp),
        extraLarge = RoundedCornerShape(1.dp),
        extraSmall = RoundedCornerShape(1.dp),
        large = RoundedCornerShape(1.dp),
    )
    val config = AppConfig(colorScheme, extendedColors, typography, shapes).also { it.InitializeWithCompose() }

    CompositionLocalProvider(
        LocalAppConfigBase provides config
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}