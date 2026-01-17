package com.eji14.cattycat.ui.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eji14.cattycat.config.LocalAppConfigBase

@Composable
fun SingleButtonDialog(
    onDismissRequest: () -> Unit,
    config: DialogConfig = LocalAppConfigBase.current.defaultDialogConfig,
    button: DialogButton
) {
    val colors = LocalDialogColors.current[config.dialogType]
        ?: error("DialogColors not found for type: $config.dialogType")
    
    BaseDialog(
        onDismissRequest = onDismissRequest,
        config = config,
        colors = colors
    ) {
        DialogPrimaryButton(
            button = button,
            colors = colors
        )
    }
}

@Composable
fun TwoButtonDialog(
    onDismissRequest: () -> Unit,
    config: DialogConfig = LocalAppConfigBase.current.defaultDialogConfig,
    secondaryButton: DialogButton,
    primaryButton: DialogButton
) {
    val colors = LocalDialogColors.current[config.dialogType]
        ?: error("DialogColors not found for type: $config.dialogType")

    BaseDialog(
        onDismissRequest = onDismissRequest,
        config = config,
        colors = colors
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
fun ThreeButtonDialog(
    onDismissRequest: () -> Unit,
    config: DialogConfig = LocalAppConfigBase.current.defaultDialogConfig,
    primaryButton: DialogButton,
    secondaryButton: DialogButton,
    tertiaryButton: DialogButton,
) {
    val colors = LocalDialogColors.current[config.dialogType]
        ?: error("DialogColors not found for type: $config.dialogType")

    BaseDialog(
        onDismissRequest = onDismissRequest,
        config = config,
        colors = colors
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