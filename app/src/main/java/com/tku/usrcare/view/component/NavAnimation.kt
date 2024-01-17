package com.tku.usrcare.view.component

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

@Composable
fun enterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition {
    return {
        slideInHorizontally(
            initialOffsetX = { 1000 }, // 從右側進入
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300))
    }
}

@Composable
fun exitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition {
    return {
        slideOutHorizontally(
            targetOffsetX = { -300 }, // 向左側退出
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    }
}

@Composable
fun popEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition {
    return {
        slideInHorizontally(
            initialOffsetX = { -300 }, // 從左側進入
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300))
    }
}

@Composable
fun popExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition {
    return {
        slideOutHorizontally(
            targetOffsetX = { 1000 }, // 向右側退出
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    }
}