package com.eji14.cattycat.ui.layout

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.eji14.cattycat.test.TodoDialog
import com.eji14.cattycat.ui.screen.ProgressState
import com.eji14.cattycat.ui.screen.ScreenHolder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasicLayoutCore(
    modifier: Modifier = Modifier,
    holder: ScreenHolder? = null,
    topBar: (@Composable (Modifier) -> Unit)? = null,
    bottomBar: (@Composable (Modifier) -> Unit)? = null,
    snackBarHost: (@Composable BoxScope.() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    attachContentToBars: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
    background: Color = Color.White,
    content: @Composable (contentModifier: Modifier, nestedScrollModifier: Modifier) -> Unit,
) {
    val activity = LocalActivity.current

    if (holder != null) {
        if (holder.enableManualExit) holder.onManualExit = { activity?.finish() }
        LaunchedEffect(Unit) {
            holder.launchEffect()
            if (activity != null) holder.todoState?.initialize(activity.applicationContext)
        }

        if (holder.todoState != null && holder.todoState.showDialog && activity != null) {
            TodoDialog(holder.todoState)
        }
    }

    ConstraintLayout(modifier = modifier
        .background(background)
        .fillMaxSize()) {
        val (topBarRef, progressRef, bottomBarRef, contentRef, loadingRef, snackBarRef) = createRefs()


        fun contentConstraints(ref: ConstrainedLayoutReference) = Modifier.constrainAs(ref) {
            top.linkTo(if (attachContentToBars && topBar != null) topBarRef.bottom else parent.top)
            bottom.linkTo(if (attachContentToBars && bottomBar != null) bottomBarRef.top else parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.fillToConstraints
            width = Dimension.fillToConstraints
        }


        val nestedScrollModifier = scrollBehavior
            ?.let { Modifier.nestedScroll(it.nestedScrollConnection) }
            ?: Modifier


        if (holder != null && holder.pullToRefreshState != null) {
            PullToRefreshBox(
                isRefreshing = holder.isRefreshing,
                onRefresh = holder::refresh,
                state = holder.pullToRefreshState,
                modifier = contentConstraints(contentRef),
            ) {
                content(Modifier
                    .fillMaxSize()
                    .padding(contentPadding), nestedScrollModifier)
            }
        } else {
            content(
                contentConstraints(contentRef)
                    .fillMaxSize()
                    .padding(contentPadding),
                nestedScrollModifier,
            )
        }

        if (holder != null && holder.showInitializeLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = contentConstraints(loadingRef).fillMaxSize(),
            ) { CircularProgressIndicator() }
        }

        topBar?.invoke(Modifier.constrainAs(topBarRef) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        if (holder != null && holder.progressState !is ProgressState.None) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(progressRef) {
                        top.linkTo(if (topBar != null) topBarRef.bottom else parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                when (val state = holder.progressState) {
                    is ProgressState.Indeterminate -> LinearProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    is ProgressState.Determinate -> LinearProgressIndicator(
                        progress = { state.progress },
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    else -> {}
                }
            }
        }

        bottomBar?.invoke(
            Modifier
                .constrainAs(bottomBarRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(top = 1.dp)
        )

        if (holder?.todoState != null) {
            val todo = createRef()
            Spacer(
                Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .size(30.dp)
                    .constrainAs(todo) { top.linkTo(parent.top); end.linkTo(parent.end) }
                    .clickable(remember { MutableInteractionSource() }, null) { holder.todoState.showDialog = true }
            )
        }

        if (snackBarHost != null) Box(Modifier.constrainAs(snackBarRef) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.windowInsetsPadding(WindowInsets.navigationBars).padding(bottom = 10.dp), content = snackBarHost)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicLayout(
    modifier: Modifier = Modifier,
    holder: ScreenHolder? = null,
    topBar: (@Composable (Modifier) -> Unit)? = null,
    bottomBar: (@Composable (Modifier) -> Unit)? = null,
    snackBarHost: (@Composable BoxScope.() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    scrollable: Boolean = true,
    attachContentToBars: Boolean = true,
    contentAlignment: Alignment.Horizontal = Alignment.Start,
    contentArrangement: Arrangement.Vertical = Arrangement.spacedBy(10.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
    background: Color = Color.White,
    content: @Composable ColumnScope.() -> Unit,
) = BasicLayoutCore(modifier, holder, topBar, bottomBar, snackBarHost, scrollBehavior, attachContentToBars, contentPadding, background) { contentMod, nestedMod ->
    Column(
        modifier = nestedMod.then(contentMod)
            .let { if (scrollable) it.verticalScroll(holder?.scrollState ?: rememberScrollState()) else it },
        verticalArrangement = contentArrangement,
        horizontalAlignment = contentAlignment,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyBasicLayout(
    modifier: Modifier = Modifier,
    holder: ScreenHolder? = null,
    topBar: (@Composable (Modifier) -> Unit)? = null,
    bottomBar: (@Composable (Modifier) -> Unit)? = null,
    snackBarHost: (@Composable BoxScope.() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    attachContentToBars: Boolean = true,
    contentAlignment: Alignment.Horizontal = Alignment.Start,
    contentArrangement: Arrangement.Vertical = Arrangement.spacedBy(10.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
    background: Color = Color.White,
    lazyListState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) = BasicLayoutCore(modifier, holder, topBar, bottomBar, snackBarHost, scrollBehavior, attachContentToBars, contentPadding, background) { contentMod, nestedMod ->
    LazyColumn(
        modifier = nestedMod.then(contentMod),
        verticalArrangement = contentArrangement,
        horizontalAlignment = contentAlignment,
        state = lazyListState,
        content = content,
    )
}