package com.eji14.cattycat.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun <P : PageNavigation.Page> NavigationHost(
    navigation: PageNavigation<P>,
    modifier: Modifier = Modifier,
    content: @Composable (P) -> Unit
) {
    BackHandler(enabled = navigation.currentState.canGoBack) {
        navigation.back()
    }

    LaunchedEffect(navigation.currentState.currentPage) {
        delay(PageNavigation.NAVIGATION_ANIMATION_DURATION.toLong())
        navigation.unlockNavigation()
    }

    DisposableEffect(Unit) {
        onDispose {
            navigation.clearStaleHolders()
        }
    }

    AnimatedContent(
        targetState = navigation.currentState.currentPage,
        transitionSpec = {
            when (navigation.navDirection) {
                PageNavigation.NavDirection.FORWARD -> {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    ) + fadeIn(
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    ) + fadeOut(
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    )
                }
                PageNavigation.NavDirection.BACKWARD -> {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    ) + fadeIn(
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    ) + fadeOut(
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    )
                }
                PageNavigation.NavDirection.REPLACE -> {
                    fadeIn(
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    ) togetherWith fadeOut(
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    )
                }
                PageNavigation.NavDirection.NONE -> {
                    fadeIn(
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    ) togetherWith fadeOut(
                        animationSpec = tween(PageNavigation.NAVIGATION_ANIMATION_DURATION)
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize(),
        label = "navigation_animation"
    ) { page ->
        content(page)
    }
}