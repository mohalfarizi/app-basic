package com.eji14.cattycat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.eji14.cattycat.config.AppConfig
import com.eji14.cattycat.config.LocalAppConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicLayout(
    modifier: Modifier = Modifier,
    holder: ScreenHolder? = null,
    attachContentToBars: Boolean = true,
    scrollable: Boolean = true,
    topBar: (@Composable (Modifier) -> Unit)? = null,
    bottomBar: (@Composable (Modifier) -> Unit)? = null,
    scrollBehaviour: TopAppBarScrollBehavior? = null,
    contentAlignment: Alignment.Horizontal = Alignment.Start,
    contentArrangement: Arrangement.Vertical = Arrangement.spacedBy(10.dp),
    contentPadding: Modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
    config: AppConfig = LocalAppConfig.current,
    content: @Composable ColumnScope.() -> Unit
) {
    val contentModifier = if (scrollBehaviour != null) {
        Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection)
    } else {
        Modifier
    }

    LaunchedEffect(Unit) {
        holder?.launch()
    }

    ConstraintLayout(
        modifier = modifier
            .background(config.colors.background)
            .fillMaxSize()
    ) {
        val (topBarRef, bottomBarRef, notificationRef, contentRef) = createRefs()

        if (holder != null && holder.refreshable) {
            PullToRefreshBox(
                isRefreshing = holder.isRefreshing,
                onRefresh = holder::refresh,
                state = holder.pullToRefreshState!!,
                modifier = contentModifier
                    .constrainAs(contentRef) {
                        top.linkTo(if (attachContentToBars && topBar != null) topBarRef.bottom else parent.top)
                        bottom.linkTo(if (attachContentToBars && bottomBar != null) bottomBarRef.top else parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .let { if (scrollable) it.verticalScroll(rememberScrollState()) else it }
                        .then(contentPadding),
                    verticalArrangement = contentArrangement,
                    horizontalAlignment = contentAlignment,
                    content = content
                )
            }
        } else {
            Column(
                modifier = contentModifier
                    .fillMaxSize()
                    .let { if (scrollable) it.verticalScroll(rememberScrollState()) else it }
                    .constrainAs(contentRef) {
                        top.linkTo(if (attachContentToBars && topBar != null) topBarRef.bottom else parent.top)
                        bottom.linkTo(if (attachContentToBars && bottomBar != null) bottomBarRef.top else parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .then(contentPadding),
                verticalArrangement = contentArrangement,
                horizontalAlignment = contentAlignment,
                content = content
            )
        }

        if (holder?.notification != null) {
            NotificationBanner(
                state = holder.notification,
                config = config,
                modifier = Modifier.constrainAs(notificationRef) {
                    if (topBar != null) top.linkTo(topBarRef.bottom)
                    else top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }

        if (topBar != null) {
            topBar(Modifier.constrainAs(topBarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        }

        if (bottomBar != null) {
            bottomBar(
                Modifier
                    .constrainAs(bottomBarRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(top = 1.dp)
                    .background(config.colors.surfaceContainerLowest)
                    .padding(10.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            )
        }
    }
}