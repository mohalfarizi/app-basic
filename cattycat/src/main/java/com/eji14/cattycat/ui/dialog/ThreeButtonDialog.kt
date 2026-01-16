package com.eji14.cattycat.ui.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun ThreeButtonDialog(
    onDismissRequest: () -> Unit,
    dialogType: DialogType,
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    primaryButton: DialogButton,
    secondaryButton: DialogButton,
    tertiaryButton: DialogButton,
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
            button = primaryButton,
            colors = colors
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        DialogTertiaryButton(
            button = secondaryButton,
            colors = colors
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        DialogTertiaryButton(
            button = tertiaryButton,
            colors = colors
        )
    }
}

@Composable
fun ThreeButtonDialog(
    onDismissRequest: () -> Unit,
    dialogType: DialogType,
    title: String,
    description: AnnotatedString,
    icon: @Composable () -> Unit,
    primaryButton: DialogButton,
    secondaryButton: DialogButton,
    tertiaryButton: DialogButton,
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
            button = primaryButton,
            colors = colors
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        DialogTertiaryButton(
            button = secondaryButton,
            colors = colors
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        DialogTertiaryButton(
            button = tertiaryButton,
            colors = colors
        )
    }
}