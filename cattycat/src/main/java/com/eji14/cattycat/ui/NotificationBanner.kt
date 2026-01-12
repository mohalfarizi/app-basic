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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eji14.cattycat.config.AppConfig
import com.eji14.cattycat.icons.CattyIcons
import com.eji14.cattycat.icons.CheckCircle
import com.eji14.cattycat.icons.Close
import com.eji14.cattycat.icons.Info
import com.eji14.cattycat.icons.Warning
import kotlinx.coroutines.delay

@Composable
fun NotificationBanner(
    state: NotificationState?,
    config: AppConfig,
    modifier: Modifier = Modifier
) {
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
                NotificationType.SUCCESS -> Triple(
                    config.colors.success,
                    config.colors.onSuccess,
                    CattyIcons.CheckCircle
                )
                NotificationType.ERROR -> Triple(
                    config.colors.error,
                    config.colors.onError,
                    CattyIcons.Warning
                )
                NotificationType.WARNING -> Triple(
                    config.colors.warning,
                    config.colors.onWarning,
                    CattyIcons.Warning
                )
                NotificationType.INFO -> Triple(
                    config.colors.primary,
                    config.colors.onPrimary,
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
                    style = config.textStyles.bodyMedium,
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