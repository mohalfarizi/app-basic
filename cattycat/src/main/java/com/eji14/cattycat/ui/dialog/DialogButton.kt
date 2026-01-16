package com.eji14.cattycat.ui.dialog

data class DialogButton(
    val text: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val countdownSeconds: Int? = null,
    val countdownFormat: (Int, String) -> String = { seconds, text -> "$text ($seconds)" }
)

enum class ButtonStyle {
    PRIMARY,
    SECONDARY,
    TERTIARY
}