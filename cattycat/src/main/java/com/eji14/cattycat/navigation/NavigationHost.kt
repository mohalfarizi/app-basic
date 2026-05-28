package com.eji14.cattycat.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

// hai

private const val ANIM_DURATION = PageNavigation.NAVIGATION_ANIMATION_DURATION

private fun slideTransition(direction: PageNavigation.NavDirection): ContentTransform =
    when (direction) {
        PageNavigation.NavDirection.FORWARD -> slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(ANIM_DURATION)
        ) + fadeIn(
            animationSpec = tween(ANIM_DURATION)
        ) togetherWith slideOutHorizontally(
            targetOffsetX = { -it / 3 },
            animationSpec = tween(ANIM_DURATION)
        ) + fadeOut(
            animationSpec = tween(ANIM_DURATION)
        )

        PageNavigation.NavDirection.BACKWARD -> slideInHorizontally(
            initialOffsetX = { -it / 3 },
            animationSpec = tween(ANIM_DURATION)
        ) + fadeIn(
            animationSpec = tween(ANIM_DURATION)
        ) togetherWith slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(ANIM_DURATION)
        ) + fadeOut(
            animationSpec = tween(ANIM_DURATION)
        )

        else -> fadeIn(
            animationSpec = tween(ANIM_DURATION)
        ) togetherWith fadeOut(
            animationSpec = tween(ANIM_DURATION)
        )
    }

private fun fadeScaleTransition(direction: PageNavigation.NavDirection): ContentTransform =
    when (direction) {
        PageNavigation.NavDirection.FORWARD ->
            (scaleIn(initialScale = 0.85f, animationSpec = tween(ANIM_DURATION)) +
                    fadeIn(animationSpec = tween(ANIM_DURATION))) togetherWith
                    (scaleOut(targetScale = 1.08f, animationSpec = tween(ANIM_DURATION)) +
                            fadeOut(animationSpec = tween(ANIM_DURATION)))

        PageNavigation.NavDirection.BACKWARD ->
            (scaleIn(initialScale = 1.08f, animationSpec = tween(ANIM_DURATION)) +
                    fadeIn(animationSpec = tween(ANIM_DURATION))) togetherWith
                    (scaleOut(targetScale = 0.85f, animationSpec = tween(ANIM_DURATION)) +
                            fadeOut(animationSpec = tween(ANIM_DURATION)))

        else -> fadeIn(
            animationSpec = tween(ANIM_DURATION)
        ) togetherWith fadeOut(
            animationSpec = tween(ANIM_DURATION)
        )
    }

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
        delay(ANIM_DURATION.toLong())
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
            val style = when (navigation.navDirection) {
                PageNavigation.NavDirection.FORWARD -> targetState.animationStyle
                PageNavigation.NavDirection.BACKWARD -> initialState.animationStyle
                else -> PageNavigation.AnimationStyle.SLIDE
            }
            when (style) {
                PageNavigation.AnimationStyle.SLIDE -> slideTransition(navigation.navDirection)
                PageNavigation.AnimationStyle.FADE_SCALE -> fadeScaleTransition(navigation.navDirection)
            }
        },
        modifier = modifier.fillMaxSize(),
        label = "navigation_animation"
    ) { page ->
        content(page)
    }
}