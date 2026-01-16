package com.eji14.cattycat.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString

@Composable
fun SingleButtonDialog(
    onDismissRequest: () -> Unit,
    dialogType: DialogType,
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    button: DialogButton,
    config: DialogConfig = DialogConfig()
) {
    val colors = LocalDialogColors.current[dialogType]
        ?: error("DialogColors not found for type: $dialogType")
    
    BaseDialog(
        onDismissRequest = onDismissRequest,
        dialogType = dialogType,
        title = title,
        description = description,
        icon = icon,
        config = config
    ) {
        DialogPrimaryButton(
            button = button,
            colors = colors
        )
    }
}

@Composable
fun SingleButtonDialog(
    onDismissRequest: () -> Unit,
    dialogType: DialogType,
    title: String,
    description: AnnotatedString,
    icon: @Composable () -> Unit,
    button: DialogButton,
    config: DialogConfig = DialogConfig()
) {
    val colors = LocalDialogColors.current[dialogType]
        ?: error("DialogColors not found for type: $dialogType")
    
    BaseDialog(
        onDismissRequest = onDismissRequest,
        dialogType = dialogType,
        title = title,
        description = description,
        icon = icon,
        config = config
    ) {
        DialogPrimaryButton(
            button = button,
            colors = colors
        )
    }
}