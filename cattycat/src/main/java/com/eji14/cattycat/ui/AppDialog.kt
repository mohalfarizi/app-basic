package com.eji14.cattycat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eji14.cattycat.config.AppConfigBase
import com.eji14.cattycat.config.LocalAppConfigBase
import com.eji14.cattycat.ui.components.MyButton
import com.eji14.cattycat.ui.components.MyText
import com.eji14.cattycat.ui.components.MyTextData
import kotlinx.coroutines.delay

@Immutable
data class DialogUI(
    val containerColor: Color,
    val titleColor: Color,
    val textColor: Color,
    val iconTint: Color,
    val titleTextData: MyTextData,
    val descriptionTextData: MyTextData,
    val buttonTextData: MyTextData,
    val iconBackground: Color = iconTint.copy(alpha = 0.2f),
    val shape: Shape = RoundedCornerShape(10.dp)
)

enum class DialogType {
    FILL, SAFE
}

class AppDialogState {
    var dialogState by mutableStateOf<DialogData?>(null)
        private set

    fun showDialog(data: DialogData) {
        dialogState = data
    }

    fun dismissDialog() {
        dialogState = null
    }
}

data class DialogData(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val confirmText: String = "OK",
    val secondaryText: String? = null,
    val tertiaryText: String? = null,
    val countdownTimer: Int = 0,
    val type: DialogType = DialogType.FILL,
    val dismissable: Boolean = true,
    val onDismissed: () -> Unit = {},
    val onConfirmed: () -> Unit = {},
    val onSecondary: () -> Unit = {},
    val onTertiary: () -> Unit = {},
    val onBack: (() -> Unit)? = null
)

@Composable
fun MyDialog(
    data: DialogData,
    onDismiss: () -> Unit,
    config: AppConfigBase = LocalAppConfigBase.current
) {
    val ui = config.dialogUI
    val confirmButtonText = if (data.countdownTimer > 0) {
        var countdown by remember(data.countdownTimer) { mutableIntStateOf(data.countdownTimer) }

        LaunchedEffect(data.countdownTimer) {
            if (data.countdownTimer > 0) {
                countdown = data.countdownTimer
                while (countdown > 0) {
                    delay(1000)
                    countdown--
                }
                if (countdown == 0) {
                    data.onConfirmed()
                }
            }
        }

        if (countdown > 0) "${data.confirmText} ($countdown)" else data.confirmText
    } else {
        data.confirmText
    }

    BackHandler(data.onBack != null) { data.onBack?.invoke() }

    Dialog(onDismissRequest = if (data.dismissable) onDismiss else {{}}) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .background(ui.containerColor, ui.shape)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = "dialog icon",
                tint = ui.iconTint,
                modifier = Modifier
                    .background(ui.iconBackground, CircleShape)
                    .padding(15.dp)
                    .size(30.dp)
            )

            MyText(
                text = data.title,
                data = ui.titleTextData
            )

            MyText(
                text = data.description,
                data = ui.descriptionTextData
            )

            when(data.type) {
                DialogType.FILL -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        MyButton(
                            text = confirmButtonText,
                            onClick = data.onConfirmed,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        if (data.secondaryText != null) MyButton(
                            text = data.secondaryText,
                            onClick = data.onSecondary,
                            config = config.tonalButtonConfig,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        if (data.tertiaryText != null) MyButton(
                            text = data.tertiaryText,
                            onClick = data.onTertiary,
                            config = config.textButtonConfig,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                DialogType.SAFE -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (data.secondaryText != null) MyButton(
                            text = data.secondaryText,
                            onClick = data.onSecondary,
                            config = config.tonalButtonConfig,
                            modifier = Modifier
                                .weight(1f)
                        )

                        MyButton(
                            text = confirmButtonText,
                            onClick = data.onConfirmed,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}