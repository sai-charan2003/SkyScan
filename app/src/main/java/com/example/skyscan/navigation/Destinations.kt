package com.example.skyscan.navigation

sealed class Destinations(val route:String) {
    object Home: Destinations("home")
    object search: Destinations("search")

}