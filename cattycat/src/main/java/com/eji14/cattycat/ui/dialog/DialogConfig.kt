package com.eji14.cattycat.ui.dialog

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.window.DialogProperties
import com.eji14.cattycat.icons.CattyIcons
import com.eji14.cattycat.icons.CheckCircle
import com.eji14.cattycat.icons.Close
import com.eji14.cattycat.icons.Info
import com.eji14.cattycat.icons.Warning

data class DialogConfig(
    val dialogType: DialogType = DialogType.INFO,
    val icon: ImageVector = when(dialogType) {
        DialogType.ERROR -> CattyIcons.Close
        DialogType.WARNING -> CattyIcons.Warning
        DialogType.INFO -> CattyIcons.Info
        DialogType.SUCCESS -> CattyIcons.CheckCircle
    },
    val title: String = "Info",
    val description: String? = null,
    val annotatedDescription: AnnotatedString? = description?.let { AnnotatedString(it) },
    val primaryButton: DialogButton = DialogButton(text = "Ok"),
    val secondaryButton: DialogButton? = null,
    val tertiaryButton: DialogButton? = null,
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true
) {
    fun toDialogProperties() = DialogProperties(
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside
    )
}