package com.eji14.cattycat.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TodoConfig.TodoAddScreen(state: TodoState, editMode: Boolean) {
    val task by state.focusedTask.collectAsState()

    var title by remember(task.id) { mutableStateOf(TextFieldValue(task.title)) }
    var description by remember(task.id) { mutableStateOf(TextFieldValue(task.description)) }

    var titleError by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp, 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("${if (editMode) "Edit" else "Create"} Task", Modifier.padding(vertical = 10.dp), style = sBigTitle)

        Text("TITLE", style = sSmallTitle)

        OutlinedTextField(title, { title = it }, Modifier.fillMaxWidth(), colors = textFieldColors, shape = defaultShape)

        if (titleError != null) Text(titleError!!, style = sError)

        Text("DESCRIPTION", Modifier.padding(top = 10.dp), style = sSmallTitle)

        OutlinedTextField(description, { description = it }, Modifier.fillMaxWidth(), colors = textFieldColors, shape = defaultShape, minLines = 5)

        Spacer(Modifier.weight(1f))

        Row(Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            OutlinedIconButton(
                state::navBack,
                Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight(),
                border = BorderStroke(1.dp, labelColor),
                colors = iconButtonColors,
                shape = defaultShape
            ) { Text("✖\uFE0F") }

            OutlinedButton(
                {
                    title = title.copy(title.text.trim())
                    description = description.copy(description.text.trim())
                    titleError = null

                    when {
                        title.text.isBlank() -> titleError = "Title cannot be empty"
                    }

                    if (titleError == null) {
                        if (editMode) {
                            state.editTask(task.id, title.text, description.text, false)
                            state.navBack()
                        }
                        else {
                            val task = state.addTask(title.text, description.text, false)
                            state.navToDetailScreen(task, true)
                        }
                    }
                },
                Modifier.weight(1f), colors = buttonColors, shape = defaultShape, contentPadding = buttonPadding
            ) { Text("${if (editMode) "Update" else "Create"} Task", style = sButton) }
        }
    }
}