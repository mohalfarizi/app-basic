package com.eji14.cattycat.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eji14.cattycat.navigation.NavigationHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TodoDialog(state: TodoState) {
    val colors = MaterialTheme.colorScheme
    val styles = MaterialTheme.typography
    val shapes = MaterialTheme.shapes

    val config = remember(colors, styles, shapes) { TodoConfig(colors, styles, shapes) }
    config.Initialize()

    Dialog(
        onDismissRequest = { state.showDialog = false }
    ) {
        with(config) {
            Box(
                Modifier.fillMaxSize()
                    .padding(vertical = 20.dp)
                    .clip(config.defaultShape)
                    .background(config.colors.background)
                    .debugDialogBackground()) {
                NavigationHost(state.navigation) { page ->
                    when (page) {
                        is TodoPages.Home -> TodoHomeScreen(state)
                        is TodoPages.Add -> TodoAddScreen(state, false)
                        is TodoPages.Detail -> TodoDetailScreen(state)
                        is TodoPages.Edit -> TodoAddScreen(state, true)
                        is TodoPages.NoteDetail -> TaskNoteDetailScreen(state)
                        is TodoPages.NoteEdit -> TaskNoteEditScreen(state)
                        is TodoPages.HomeNoteDetail -> HomeNoteDetailScreen(state)
                        is TodoPages.HomeNoteEdit -> HomeNoteEditScreen(state)
                    }
                }
            }
        }
    }
}

@Composable
internal fun Modifier.debugDialogBackground(): Modifier {
    val colors = MaterialTheme.colorScheme

    val bgColor = colors.surface
    val dotColor = colors.primary.copy(alpha = 0.08f)

    // blobs use primary, secondary, tertiary — the three main M3 color roles
    val blobPrimary = colors.primaryContainer.copy(alpha = 0.60f)
    val blobSecondary = colors.secondaryContainer.copy(alpha = 0.50f)
    val blobTertiary = colors.tertiaryContainer.copy(alpha = 0.45f)

    // rings follow primary but very faint
    val ringColor = colors.primary.copy(alpha = 0.10f)

    return this
        .drawBehind {
            val width = size.width
            val height = size.height

            drawRect(color = bgColor)

            // dot grid
            val dotSpacing = 20.dp.toPx()
            val dotRadius = 1.5.dp.toPx()
            var x = 0f
            while (x < width) {
                var y = 0f
                while (y < height) {
                    drawCircle(color = dotColor, radius = dotRadius, center = Offset(x, y))
                    y += dotSpacing
                }
                x += dotSpacing
            }

            // blob 1 — primaryContainer, top-right
            drawCircle(
                color = blobPrimary,
                radius = 130.dp.toPx(),
                center = Offset(width - 20.dp.toPx(), 20.dp.toPx())
            )

            // blob 2 — secondaryContainer, bottom-left
            drawCircle(
                color = blobSecondary,
                radius = 100.dp.toPx(),
                center = Offset(20.dp.toPx(), height - 20.dp.toPx())
            )

            // blob 3 — tertiaryContainer, left mid
            drawCircle(
                color = blobTertiary,
                radius = 65.dp.toPx(),
                center = Offset(10.dp.toPx(), height * 0.30f)
            )

            // rings — top-right
            listOf(190.dp.toPx(), 125.dp.toPx()).forEach { radius ->
                drawCircle(
                    color = ringColor,
                    radius = radius,
                    center = Offset(width - 20.dp.toPx(), 20.dp.toPx()),
                    style = Stroke(width = 0.5.dp.toPx())
                )
            }

            // ring — bottom-left
            drawCircle(
                color = ringColor,
                radius = 130.dp.toPx(),
                center = Offset(20.dp.toPx(), height - 20.dp.toPx()),
                style = Stroke(width = 0.5.dp.toPx())
            )
        }
}