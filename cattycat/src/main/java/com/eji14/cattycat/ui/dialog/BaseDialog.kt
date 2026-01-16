package com.eji14.cattycat.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@Composable
internal fun BaseDialog(
    onDismissRequest: () -> Unit,
    dialogType: DialogType,
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    config: DialogConfig,
    buttons: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalDialogColors.current[dialogType]
        ?: error("DialogColors not found for type: $dialogType")

    Dialog(
        onDismissRequest = {
            if (config.dismissOnClickOutside) {
                onDismissRequest()
            }
        },
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
                    icon()
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.titleColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = colors.descriptionColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
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
internal fun BaseDialog(
    onDismissRequest: () -> Unit,
    dialogType: DialogType,
    title: String,
    description: AnnotatedString,
    icon: @Composable () -> Unit,
    config: DialogConfig,
    buttons: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalDialogColors.current[dialogType]
        ?: error("DialogColors not found for type: $dialogType")

    Dialog(
        onDismissRequest = {
            if (config.dismissOnClickOutside) {
                onDismissRequest()
            }
        },
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
                    icon()
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.titleColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = colors.descriptionColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
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

    val isEnabled = button.enabled

    Button(
        onClick = button.onClick,
        enabled = isEnabled,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primaryButtonColor,
            contentColor = colors.primaryButtonTextColor,
            disabledContainerColor = colors.primaryButtonColor.copy(alpha = 0.6f),
            disabledContentColor = colors.primaryButtonTextColor.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = displayText,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
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

    val isEnabled = button.enabled && (button.countdownSeconds == null || remainingSeconds == 0)

    OutlinedButton(
        onClick = button.onClick,
        enabled = isEnabled,
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
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = displayText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
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

    val isEnabled = button.enabled && (button.countdownSeconds == null || remainingSeconds == 0)

    TextButton(
        onClick = button.onClick,
        enabled = isEnabled,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = colors.secondaryButtonTextColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = colors.secondaryButtonTextColor.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = displayText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}