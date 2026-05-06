package com.eji14.cattycat.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TodoConfig.TodoHomeScreen(state: TodoState) {
    val tasks by state.tasks.collectAsState()
    val hideDone by state.hideDone.collectAsState()
    val taskDoneCount by state.taskDoneCount.collectAsState()
    val taskDoneProgress by state.taskDoneProgress.collectAsState()

    Column(
        Modifier.padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                "DEBUG",
                Modifier
                    .background(colors.surfaceContainer, defaultShape)
                    .border(1.dp, labelColor, defaultShape)
                    .padding(5.dp, 2.dp),
                style = sLabel,
                color = titleColor
            )

            Text(
                state.title,
                Modifier
                    .clickable(remember { MutableInteractionSource() }, null) { state.toggleSharedPreferences() }
                    .weight(1f),
                style = sLabel
            )

            OutlinedButton(state::navToAddScreen, colors = buttonColors, shape = defaultShape) { Text("Add", style = sButton) }
        }

        Row(Modifier.padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Screen Checklist",
                style = sBigTitle
            )

            Text(" \uD83D\uDCC4", Modifier.clickable(remember { MutableInteractionSource() }, null) { state.navToHomeNoteDetail() }, style = sButton)
        }

        Text(
            "Your project screen todo list",
            style = sLabel
        )

        HorizontalDivider(Modifier.padding(top = 20.dp), color = labelColor)

        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .fillMaxWidth()
        ) {
            Spacer(Modifier.height(20.dp))
            tasks.forEachIndexed { index, task ->
                Row(
                    Modifier
                        .padding(horizontal = 10.dp)
                        .clickable(remember { MutableInteractionSource() }, null) {
                            state.navToDetailScreen(task)
                        }
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(if (task.isDone) "\uD83D\uDFE2" else "⚪", Modifier.padding(5.dp))
                    Column(Modifier.weight(1f)) {
                        Text(task.title, style = sTitle)
                        Text(task.description, style = sDesc)
                    }
                }

                if (index < tasks.lastIndex) HorizontalDivider(Modifier.padding(10.dp, 10.dp))
            }
        }

        Row(
            Modifier
                .padding(bottom = 5.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text("Progress", style = sLabel)
            Text(
                "$taskDoneCount done ${if (hideDone) "\uD83D\uDE48" else "\uD83D\uDE4A"}",
                Modifier.clickable(remember { MutableInteractionSource() }, null) { state.toggleHideDone() },
                style = sLabel,
                color = titleColor
            )
        }
        LinearProgressIndicator({ taskDoneProgress }, Modifier.fillMaxWidth())

        OutlinedButton(
            state::closeDialog, Modifier
                .align(Alignment.End)
                .padding(vertical = 10.dp),
            colors = buttonColors,
            shape = defaultShape
        ) { Text("DISMISS", style = sButton) }
    }
}