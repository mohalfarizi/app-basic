package com.eji14.appbasic.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eji14.cattycat.config.AppConfigBase
import com.eji14.cattycat.config.LocalAppConfigBase
import com.eji14.cattycat.ui.components.MyButton
import com.eji14.cattycat.ui.components.MyText

@Composable
fun TextShowCase(
    modifier: Modifier = Modifier,
    config: AppConfigBase = LocalAppConfigBase.current
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        MyText(
            text = "This is title",
            data = config.textTitle
        )

        MyText(
            text = "This is content",
            data = config.textContent
        )

        MyText(
            text = "This is label",
            data = config.textLabel
        )

        MyButton(
            text = "Click Me",
            onClick = {},
            config = config.buttonConfig
        )

        MyButton(
            text = "Click Me",
            onClick = {},
            config = config.tonalButtonConfig
        )

        MyButton(
            text = "Click Me",
            onClick = {},
            config = config.outlinedButtonConfig
        )

        MyButton(
            text = "Click Me",
            onClick = {},
            config = config.textButtonConfig
        )
    }
}