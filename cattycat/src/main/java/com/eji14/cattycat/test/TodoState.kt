package com.eji14.cattycat.test

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.eji14.cattycat.navigation.PageNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class TodoDialogTask(val title: String, val description: String, val isDone: Boolean, val id: Int = 0, val notes: List<String> = listOf(""))

internal sealed class TodoPages(
    identifier: String,
    routeKey: String = identifier,
    holder: PageNavigation.Holder = PageNavigation.Holder.DEFAULT,
    animationStyle: PageNavigation.AnimationStyle = PageNavigation.AnimationStyle.SLIDE
) : PageNavigation.Page(identifier, routeKey, holder, animationStyle) {
    object Home : TodoPages("Home")
    object Detail : TodoPages("Detail")
    object Edit : TodoPages("Edit")
    object Add : TodoPages("Add")
    object NoteDetail : TodoPages("NoteDetail")
    object NoteEdit : TodoPages("NoteEdit")
    object HomeNoteDetail : TodoPages("HomeNoteDetail")
    object HomeNoteEdit : TodoPages("HomeNoteEdit")
}

class TodoConfig(
    val colors: ColorScheme,
    styles: Typography,
    shapes: Shapes
) {
    val labelColor = colors.outlineVariant

    val descriptionColor = colors.outline
    val titleColor = colors.onSurface
    val dividerColor = colors.outlineVariant
    val defaultShape = shapes.medium

    val buttonColors = ButtonColors(colors.background, colors.primary, colors.background, colors.primary.copy(0.5f))

    val iconButtonColors = IconButtonColors(colors.background, colors.primary, colors.background, colors.primary.copy(0.5f))
    val buttonPadding = PaddingValues(12.dp, 18.dp)
    val narrowButtonPadding = PaddingValues(5.dp, 12.dp)

    // sTitle, sBigTitle, sSmallTitle, sLabel, sError, sButton, sDesc, sContent, sHeadline
    val sTitle = styles.titleMedium.copy(color = titleColor, fontWeight = FontWeight.Bold)
    val sBigTitle = styles.titleLarge.copy(color = titleColor, fontWeight = FontWeight.Bold)
    val sSmallTitle = styles.labelSmall.copy(color = descriptionColor)
    val sLabel = styles.labelLarge.copy(color = labelColor)
    val sError = styles.labelSmall.copy(color = colors.error)
    val sButton = styles.bodyLarge.copy(fontWeight = FontWeight.Medium)
    val sDesc = styles.bodyMedium.copy(color = descriptionColor)
    val sContent = styles.bodyLarge.copy(color = titleColor)
    val sHeadline = styles.headlineMedium.copy(color = titleColor, fontWeight = FontWeight.Bold)

    lateinit var textFieldColors: TextFieldColors

    @Composable
    fun Initialize() {
        if (!::textFieldColors.isInitialized) textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.outline,
            unfocusedBorderColor = colors.outlineVariant,
            focusedContainerColor = colors.background,
            unfocusedContainerColor = colors.background,
            focusedTextColor = colors.onSurface,
            unfocusedTextColor = colors.onSurface,
            focusedPlaceholderColor = colors.outlineVariant,
            unfocusedPlaceholderColor = colors.outlineVariant,
            cursorColor = colors.primary
        )
    }
}

class TodoState(val name: String) {
    private val scope = CoroutineScope(Dispatchers.IO)
    var title by mutableStateOf(name)
    var notes = MutableStateFlow(listOf(""))
    var noteIndex = MutableStateFlow(0)
    var showDialog by mutableStateOf(false)
    var isGlobal by mutableStateOf(false)

    private val currentKey get() = if (isGlobal) "global" else name

    internal val navigation = PageNavigation<TodoPages>(TodoPages.Home)

    val allTasks = MutableStateFlow<List<TodoDialogTask>>(emptyList())
    val hideDone = MutableStateFlow(true)
    val focusedTaskId = MutableStateFlow(0)
    val focusedNoteIndex = MutableStateFlow(0)

    val tasks = allTasks.combine(hideDone) { allTasks, hideDone1 ->
        allTasks.let { if (hideDone1) it.filter { t -> !t.isDone } else it }
    }.stateIn(scope, SharingStarted.WhileSubscribed(1000), emptyList())

    val focusedTask = allTasks.combine(focusedTaskId) { allTasks, focusedTaskId ->
        allTasks.firstOrNull { it.id == focusedTaskId } ?: TodoDialogTask("", "", false)
    }.stateIn(scope, SharingStarted.WhileSubscribed(1000), TodoDialogTask("", "", false))

    val taskDoneCount = allTasks.map {
        val done = it.count { t -> t.isDone }
        val total = it.size
        "$done/$total"
    }.stateIn(scope, SharingStarted.WhileSubscribed(1000), "")

    val taskDoneProgress = allTasks.map {
        if (it.isEmpty()) 0f else it.count { it.isDone }.toFloat() / it.size
    }.stateIn(scope, SharingStarted.WhileSubscribed(1000), 0f)

    private var sharedPrefs: SharedPreferences? = null
    private var globalPrefs: SharedPreferences? = null
    private val activePrefs get() = if (isGlobal) globalPrefs else sharedPrefs

    fun initialize(context: Context) {
        if (sharedPrefs == null) sharedPrefs = context.applicationContext
            .getSharedPreferences(name, Context.MODE_PRIVATE)

        if (globalPrefs == null) globalPrefs = context.applicationContext
            .getSharedPreferences("cattycat_global", Context.MODE_PRIVATE)

        load()
    }
    private fun load() {
        val loaded = activePrefs!!.getStringSet("$currentKey-tasks", null)
            ?.toSet()
            ?.mapNotNull {
                val split = it.split("(**||**)")
                if (split.size < 5) return@mapNotNull null
                val notes = split[4].split("[**||**]")
                TodoDialogTask(split[0], split[1], split[2].toBoolean(), split[3].toInt(), notes)
            } ?: emptyList()
        allTasks.value = loaded.sortedBy { it.id }
        val raw = activePrefs?.getString("$currentKey-notes", null)
        notes.value = raw?.split("[**||**]") ?: listOf("")
    }

    private fun saveTasks() {
        val serialized = allTasks.value.map {
            val notesJoined = it.notes.joinToString("[**||**]")
            "${it.title}(**||**)${it.description}(**||**)${it.isDone}(**||**)${it.id}(**||**)$notesJoined"
        }.toSet()
        activePrefs?.edit { putStringSet("$currentKey-tasks", serialized) }
    }

    private fun saveNotes() {
        val notesJoined = notes.value.joinToString("[**||**]")
        activePrefs?.edit { putString("$currentKey-notes", notesJoined) }
    }

    fun toggleSharedPreferences() {
        isGlobal = !isGlobal
        title = currentKey
        load()
    }

    fun toggleHideDone() {
        hideDone.value = !hideDone.value
    }

    fun addTask(title: String, description: String, isDone: Boolean = false) =
        TodoDialogTask(title, description, isDone, (allTasks.value.maxOfOrNull { it.id } ?: 0) + 1).also { task ->
            allTasks.value = (allTasks.value + task).sortedBy { it.id }
            focusedTaskId.value = task.id
            saveTasks()
        }

    fun deleteTask(id: Int) {
        allTasks.value = allTasks.value.filter { it.id != id }
        saveTasks()
    }

    fun toggleTask(id: Int, isDone: Boolean) {
        allTasks.value = allTasks.value.map { if (it.id == id) it.copy(isDone = isDone) else it }
        saveTasks()
    }

    fun editTask(id: Int, title: String, description: String, isDone: Boolean) {
        allTasks.value = allTasks.value.map {
            if (it.id == id) it.copy(title = title, description = description, isDone = isDone) else it
        }
        saveTasks()
    }

    fun updateFocusedNote(index: Int) {
        focusedNoteIndex.value = index
    }

    fun updateFocusedHomeNote(index: Int) {
        noteIndex.value = index
    }

    fun editNote(note: String) {
        val raw = focusedTask.value
        val updated = raw.notes
            .mapIndexed { i, s -> if (i == focusedNoteIndex.value) note else s }
            .filter { it.isNotEmpty() }
            .ifEmpty { listOf("") }
        val task = raw.copy(notes = if (updated.last().isNotEmpty()) updated + "" else updated)
        allTasks.value = allTasks.value.map { if (it.id == task.id) task else it }
        saveTasks()
    }


    fun editHomeNote(note: String) {
        val updated = notes.value
            .mapIndexed { i, s -> if (i == noteIndex.value) note else s }
            .filter { it.isNotEmpty() }
            .ifEmpty { listOf("") }
        notes.value = if (updated.last().isNotEmpty()) updated + "" else updated
        saveNotes()
    }

    fun navToAddScreen() {
        navigation.navigateTo(TodoPages.Add)
        focusedTaskId.value = 0
    }
    fun navToDetailScreen(task: TodoDialogTask, replace: Boolean = false) {
        focusedTaskId.value = task.id
        focusedNoteIndex.value = 0
        if (replace) navigation.replace(TodoPages.Detail, mapOf("detailId" to task.id), PageNavigation.NavDirection.FORWARD)
        else navigation.navigateTo(TodoPages.Detail, mapOf("detailId" to task.id))
    }
    fun navToEditScreen() { navigation.navigateTo(TodoPages.Edit) }
    fun navToNoteDetail() {
        val task = focusedTask.value
        if (task.notes.size == 1) navigation.navigateTo(TodoPages.NoteEdit)
        else navigation.navigateTo(TodoPages.NoteDetail)
    }
    fun navToNoteEdit() { navigation.navigateTo(TodoPages.NoteEdit) }
    fun navToHomeNoteDetail() {
        navigation.navigateTo(TodoPages.HomeNoteDetail)
        noteIndex.value = 0
    }
    fun navToHomeNoteEdit() { navigation.navigateTo(TodoPages.HomeNoteEdit) }
    fun navBack() { navigation.back() }
    fun navBackToDetail() { navigation.replace(TodoPages.NoteDetail, direction = PageNavigation.NavDirection.BACKWARD) }
    fun closeDialog() { showDialog = false }
}