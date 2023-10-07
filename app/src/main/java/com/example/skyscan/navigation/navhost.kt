package com.example.skyscan.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skyscan.search
import com.example.skyscan.weatherdata
import com.example.skyscan.weatherscreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun navhost(navController:NavHostController) {
    val context= LocalContext.current
    NavHost(navController = navController, startDestination = Destinations.Home.route, enterTransition = { EnterTransition.None}, exitTransition = { ExitTransition.None}){
        composable(Destinations.Home.route){
            weatherdata(navController)
        }
        composable(Destinations.search.route, enterTransition = {
            fadeIn(
                animationSpec = tween(
                    200, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
            exitTransition = {fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                animationSpec = tween(300, easing = LinearEasing),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )}){
            search(navController)
        }
    }

}