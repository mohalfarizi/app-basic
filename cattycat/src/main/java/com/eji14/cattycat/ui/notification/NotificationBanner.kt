package com.eji14.cattycat.ui.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eji14.cattycat.core.config.AppConfigBase
import com.eji14.cattycat.core.config.LocalAppConfigBase
import com.eji14.cattycat.ui.screen.NotifType
import com.eji14.cattycat.ui.screen.NotificationState
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

@Composable
fun NotificationBannerStack(
    notifications: List<NotificationState>,
    modifier: Modifier = Modifier,
    config: AppConfigBase = LocalAppConfigBase.current,
    colors: NotificationColors = config.getNotificationColors(),
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        notifications.forEach { state ->
            key(state.id) {
                NotificationBannerItem(
                    state = state,
                    config = config,
                    colors = colors,
                )
            }
        }
    }
}

@Composable
private fun NotificationBannerItem(
    state: NotificationState,
    config: AppConfigBase,
    colors: NotificationColors,
) {
    var visible by remember(state.version) { mutableStateOf(true) }

    LaunchedEffect(state.version) {
        visible = true
        delay(state.duration)
        if (visible) {
            visible = false
            delay(300)
            state.onDismiss()
        }
    }


    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {


        val (backgroundColor, contentColor) = when (state.type) {
            NotifType.SUCCESS -> Pair(colors.success, colors.onSuccess)
            NotifType.ERROR -> Pair(colors.error, colors.onError)
            NotifType.WARNING -> Pair(colors.warning, colors.onWarning)
            NotifType.INFO -> Pair(colors.info, colors.onInfo)
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = state.message,
                color = contentColor,
                style = config.subStyles.bodyMedium,
                modifier = Modifier.weight(1f),
            )

            if (state.actionLabel != null && state.onAction != null) {
                TextButton(
                    onClick = {
                        state.onAction.invoke()
                        state.onDismiss()
                    }
                ) {
                    Text(text = state.actionLabel, color = contentColor)
                }
            }

            IconButton(
                onClick = {
                    visible = false
                    state.onDismiss()
                }
            ) { Text("X") }
        }
    }
}