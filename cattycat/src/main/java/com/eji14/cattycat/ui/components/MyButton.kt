package com.eji14.cattycat.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.eji14.cattycat.config.LocalAppConfigBase

data class ButtonConfig(
    val shape: Shape,
    val colors: ButtonColors,
    val elevation: ButtonElevation?,
    val border: BorderStroke?,
    val contentPadding: PaddingValues,
    val centered: Boolean
)

@Composable
fun MyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    config: ButtonConfig = LocalAppConfigBase.current.buttonConfig,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = config.shape,
        colors = config.colors,
        elevation = config.elevation,
        border = config.border,
        contentPadding = config.contentPadding
    ) {
        Text(
            text = "Click Me"
        )
    }
}