package com.eji14.cattycat.test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TodoConfig.CoreNoteDetailScreen(
    title: String,
    notes: List<String>,
    index: Int,
    onIndexChange: (Int) -> Unit,
    onBack: () -> Unit,
    onNavToEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gap = 25.dp

    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { notes.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        onIndexChange(pagerState.currentPage)
    }

    LaunchedEffect(index) {
        if (pagerState.currentPage != index) pagerState.scrollToPage(index)
    }

    Column(
        modifier
            .fillMaxSize()
            .debugDialogBackground()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp, bottom = 15.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {
            FilledTonalIconButton(onBack, Modifier
                .align(Alignment.CenterStart)
                .padding(vertical = 20.dp), shape = defaultShape) { Text("<") }
            IconButton(onNavToEdit, Modifier
                .align(Alignment.CenterEnd)
                .padding(vertical = 20.dp), shape = defaultShape) { Text("✏\uFE0F") }
            Text("Preview", Modifier.align(Alignment.Center), style = sTitle)
        }

        Text("Note · $title", style = sLabel)

        HorizontalDivider(Modifier.padding(top = gap), color = labelColor)

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(Modifier.fillMaxSize()) {
                Text(
                    notes.getOrNull(page) ?: "",
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f)
                        .padding(vertical = 20.dp),
                    style = sContent
                )
                Text((page + 1).toString(), Modifier.align(Alignment.CenterHorizontally))
            }
        }

        HorizontalDivider(Modifier.padding(bottom = 5.dp), color = labelColor)
    }
}

@Composable
fun TodoConfig.CoreNoteEditScreen(
    initialNote: String,
    onBack: () -> Unit,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var note by remember { mutableStateOf(TextFieldValue(initialNote)) }

    Column(
        modifier
            .fillMaxSize()
            .debugDialogBackground()
            .padding(20.dp, 10.dp)
    ) {
        Box(Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)) {
            Text("Edit Note", Modifier
                .align(Alignment.Center)
                .padding(vertical = 10.dp), style = sTitle)
            FilledTonalIconButton(onBack, Modifier.align(Alignment.CenterStart), shape = defaultShape) { Text("<", style = sButton) }
            IconButton({
                onSave(note.text)
            }, Modifier.align(Alignment.CenterEnd), shape = defaultShape) { Text("\uD83D\uDCBE", style = sButton) }
        }

        HorizontalDivider(color = dividerColor)

        BasicTextField(
            value = note,
            { note = it },
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            textStyle = sContent
        )

        HorizontalDivider(color = dividerColor)
    }
}

// --- Task-specific screens ---

@Composable
fun TodoConfig.TaskNoteDetailScreen(
    state: TodoState,
    modifier: Modifier = Modifier
) {
    val task by state.focusedTask.collectAsState()
    val index by state.focusedNoteIndex.collectAsState()

    CoreNoteDetailScreen(
        title = task.title,
        notes = task.notes,
        index = index,
        onIndexChange = state::updateFocusedNote,
        onBack = state::navBack,
        onNavToEdit = state::navToNoteEdit,
        modifier = modifier
    )
}

@Composable
fun TodoConfig.TaskNoteEditScreen(
    state: TodoState,
    modifier: Modifier = Modifier
) {
    val task by state.focusedTask.collectAsState()
    val index by state.focusedNoteIndex.collectAsState()

    CoreNoteEditScreen(
        initialNote = task.notes[index],
        onBack = state::navBack,
        onSave = { text ->
            state.editNote(text)
            state.navBackToDetail()
        },
        modifier = modifier
    )
}

// --- Home-specific screens ---

@Composable
fun TodoConfig.HomeNoteDetailScreen(
    state: TodoState,
    modifier: Modifier = Modifier
) {
    val notes by state.notes.collectAsState()
    val index by state.noteIndex.collectAsState()
    CoreNoteDetailScreen(
        title = "Home",
        notes = notes,
        index = index,
        onIndexChange = state::updateFocusedHomeNote,
        onBack = state::navBack,
        onNavToEdit = state::navToHomeNoteEdit,
        modifier = modifier
    )
}

@Composable
fun TodoConfig.HomeNoteEditScreen(
    state: TodoState,
    modifier: Modifier = Modifier
) {
    val notes by state.notes.collectAsState()
    val noteIndex by state.noteIndex.collectAsState()
    CoreNoteEditScreen(
        initialNote = notes[noteIndex],
        onBack = state::navBack,
        onSave = { text ->
            state.editHomeNote(text)
            state.navBack()
        },
        modifier = modifier
    )
}