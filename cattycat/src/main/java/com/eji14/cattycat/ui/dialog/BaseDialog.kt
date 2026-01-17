package com.eji14.cattycat.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eji14.cattycat.ui.components.Button
import com.eji14.cattycat.ui.components.OutlinedButton
import com.eji14.cattycat.ui.components.TextButton
import kotlinx.coroutines.delay

@Composable
internal fun BaseDialog(
    onDismissRequest: () -> Unit,
    config: DialogConfig,
    colors: DialogColors,
    buttons: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = { if (config.dismissOnBackPress) onDismissRequest() },
        properties = config.toDialogProperties()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.backgroundColor, RoundedCornerShape(12.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(colors.iconBackgroundColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = config.icon,
                        contentDescription = null,
                        tint = colors.iconColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = config.title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (config.annotatedDescription != null) Text(
                    text = config.annotatedDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    buttons()
                }
            }
        }
    }
}

@Composable
internal fun DialogPrimaryButton(
    button: DialogButton,
    colors: DialogColors,
    modifier: Modifier = Modifier
) {
    var remainingSeconds by remember(button.countdownSeconds) {
        mutableStateOf(button.countdownSeconds ?: 0)
    }

    LaunchedEffect(button.countdownSeconds) {
        if (button.countdownSeconds != null && button.countdownSeconds > 0) {
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
            }
            button.onClick()
        }
    }

    val displayText = if (button.countdownSeconds != null && remainingSeconds > 0) {
        button.countdownFormat(remainingSeconds, button.text)
    } else {
        button.text
    }


    Button(
        onClick = button.onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primaryButtonColor,
            contentColor = colors.primaryButtonTextColor,
            disabledContainerColor = colors.primaryButtonColor.copy(alpha = 0.6f),
            disabledContentColor = colors.primaryButtonTextColor.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
internal fun DialogSecondaryButton(
    button: DialogButton,
    colors: DialogColors,
    modifier: Modifier = Modifier
) {
    var remainingSeconds by remember(button.countdownSeconds) {
        mutableStateOf(button.countdownSeconds ?: 0)
    }

    LaunchedEffect(button.countdownSeconds) {
        if (button.countdownSeconds != null && button.countdownSeconds > 0) {
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
            }
            button.onClick()
        }
    }

    val displayText = if (button.countdownSeconds != null && remainingSeconds > 0) {
        button.countdownFormat(remainingSeconds, button.text)
    } else {
        button.text
    }

    OutlinedButton(
        onClick = button.onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = colors.secondaryButtonTextColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = colors.secondaryButtonTextColor.copy(alpha = 0.6f)
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(colors.secondaryButtonBorderColor)
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
internal fun DialogTertiaryButton(
    button: DialogButton,
    colors: DialogColors,
    modifier: Modifier = Modifier
) {
    var remainingSeconds by remember(button.countdownSeconds) {
        mutableStateOf(button.countdownSeconds ?: 0)
    }

    LaunchedEffect(button.countdownSeconds) {
        if (button.countdownSeconds != null && button.countdownSeconds > 0) {
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
            }
            button.onClick()
        }
    }

    val displayText = if (button.countdownSeconds != null && remainingSeconds > 0) {
        button.countdownFormat(remainingSeconds, button.text)
    } else {
        button.text
    }


    TextButton(
        onClick = button.onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = colors.secondaryButtonTextColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = colors.secondaryButtonTextColor.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelLarge
        )
    }
}