package com.eji14.cattycat.ui.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun TwoButtonDialog(
    onDismissRequest: () -> Unit,
    dialogType: DialogType,
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    secondaryButton: DialogButton,
    primaryButton: DialogButton,
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
        Row {
            DialogSecondaryButton(
                button = secondaryButton,
                colors = colors,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            DialogPrimaryButton(
                button = primaryButton,
                colors = colors,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TwoButtonDialog(
    onDismissRequest: () -> Unit,
    dialogType: DialogType,
    title: String,
    description: AnnotatedString,
    icon: @Composable () -> Unit,
    secondaryButton: DialogButton,
    primaryButton: DialogButton,
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
        Row {
            DialogSecondaryButton(
                button = secondaryButton,
                colors = colors,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            DialogPrimaryButton(
                button = primaryButton,
                colors = colors,
                modifier = Modifier.weight(1f)
            )
        }
    }
}