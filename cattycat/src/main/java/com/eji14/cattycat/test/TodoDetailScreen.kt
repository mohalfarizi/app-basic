package com.eji14.cattycat.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TodoConfig.TodoDetailScreen(state: TodoState) {
    val task by state.focusedTask.collectAsState()
    var isDone by remember(task.id) { mutableStateOf(task.isDone) }

    Column(Modifier.fillMaxSize().padding(20.dp, 30.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilledTonalIconButton(state::navBack, shape = defaultShape) { Text("<") }

            OutlinedButton(
                state::navToNoteDetail,
                shape = defaultShape,
                colors = buttonColors
            ) { Text("\uD83D\uDCC4", style = sButton) }
        }

        Text(task.title, style = sHeadline)

        HorizontalDivider(color = labelColor)
        
        Text("DESCRIPTION", style = sSmallTitle)

        Text(task.description, style = sContent)

        Spacer(Modifier.weight(1f))

        Column(
            Modifier
                .background(colors.background, defaultShape)
                .border(1.dp, labelColor, defaultShape)
                .padding(10.dp)
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Switch(isDone, {
                    isDone = it
                    state.toggleTask(task.id, it)
                })

                Column {
                    Text("Mark as done", style = sTitle)
                    Text("Tap to toggle", style = sLabel)
                }
            }

            HorizontalDivider(color = labelColor)

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton({
                    state.deleteTask(task.id)
                    state.navBack()
                }, Modifier.weight(1f), contentPadding = narrowButtonPadding, colors = buttonColors, shape = defaultShape) {
                    Text("\uD83D\uDDD1", style = sButton)
                }

                OutlinedButton(state::navToEditScreen, Modifier.weight(3f), contentPadding = narrowButtonPadding, colors = buttonColors, shape = defaultShape) {
                    Text("Edit", style = sButton)
                }
            }
        }
    }
}