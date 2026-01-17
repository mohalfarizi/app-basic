package com.eji14.cattycat.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eji14.cattycat.config.AppConfigBase
import com.eji14.cattycat.icons.CattyIcons
import com.eji14.cattycat.icons.CheckCircle
import com.eji14.cattycat.icons.Close
import com.eji14.cattycat.icons.Info
import com.eji14.cattycat.icons.Warning
import com.eji14.cattycat.ui.theme.ExtendedColorScheme
import kotlinx.coroutines.delay

@Immutable
data class NotificationColors(
    val success: Color,
    val onSuccess: Color,
    val error: Color,
    val onError: Color,
    val warning: Color,
    val onWarning: Color,
    val info: Color,
    val onInfo: Color
)

object NotificationDefaults {
    fun notificationColors(colors: ColorScheme, extendedColors: ExtendedColorScheme): NotificationColors {
        val warning = extendedColors.warning
        val info = extendedColors.info
        val success = extendedColors.success

        return NotificationColors(
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
}

val LocalNotificationColors = compositionLocalOf<NotificationColors> {
    error("No NotificationColors provided")
}

@Composable
fun NotificationBanner(
    state: NotificationState?,
    config: AppConfigBase,
    modifier: Modifier = Modifier
) {
    val colors = LocalNotificationColors.current
    var visible by remember(state) { mutableStateOf(state != null) }

    LaunchedEffect(state) {
        if (state != null) {
            visible = true
            delay(state.duration)
            visible = false
            delay(300)
            state.onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible && state != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        if (state != null) {
            val (backgroundColor, contentColor, icon) = when (state.type) {
                NotifType.SUCCESS -> Triple(
                    colors.success,
                    colors.onSuccess,
                    CattyIcons.CheckCircle
                )
                NotifType.ERROR -> Triple(
                    colors.error,
                    colors.onError,
                    CattyIcons.Warning
                )
                NotifType.WARNING -> Triple(
                    colors.warning,
                    colors.onWarning,
                    CattyIcons.Warning
                )
                NotifType.INFO -> Triple(
                    colors.info,
                    colors.onInfo,
                    CattyIcons.Info
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(backgroundColor, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor
                )

                Text(
                    text = state.message,
                    color = contentColor,
                    style = config.styles.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                if (state.actionLabel != null && state.onAction != null) {
                    TextButton(
                        onClick = {
                            state.onAction.invoke()
                            state.onDismiss()
                        }
                    ) {
                        Text(
                            text = state.actionLabel,
                            color = contentColor
                        )
                    }
                }

                IconButton(
                    onClick = {
                        visible = false
                        state.onDismiss()
                    }
                ) {
                    Icon(
                        imageVector = CattyIcons.Close,
                        contentDescription = "Dismiss",
                        tint = contentColor
                    )
                }
            }
        }
    }
}