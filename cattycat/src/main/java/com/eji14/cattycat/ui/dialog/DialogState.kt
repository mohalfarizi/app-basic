package com.eji14.cattycat.ui.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class DialogState {
    var shownDialog by mutableStateOf<DialogConfig?>(null)
        private set
    private var onDismissed: (() -> Unit)? = null

    fun popupDialog(
        config: DialogConfig,
        onDismissed: () -> Unit = {},
        onPrimaryClicked: () -> Unit = {},
        onSecondaryClicked: () -> Unit = {},
        onTertiaryClicked: () -> Unit = {}
    ) {
        this.onDismissed = onDismissed
        shownDialog = config.copy(
            primaryButton = config.primaryButton.copy(onClick = onPrimaryClicked),
            secondaryButton = config.secondaryButton?.copy(onClick = onSecondaryClicked),
            tertiaryButton = config.tertiaryButton?.copy(onClick = onTertiaryClicked),
        )
    }

    fun dismiss() {
        if (shownDialog != null) {
            shownDialog = null
            onDismissed?.invoke()
        }
    }
}