package com.eji14.cattycat.ui.dialog

import androidx.compose.ui.window.DialogProperties

data class DialogConfig(
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true
) {
    fun toDialogProperties() = DialogProperties(
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside
    )
}