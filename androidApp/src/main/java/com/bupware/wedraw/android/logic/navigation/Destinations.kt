package com.bupware.wedraw.android.logic.navigation

sealed class Destinations (val ruta : String) {

    object SplashScreen: Destinations("SplashScreen")
    object DrawingScreen: Destinations("DrawingScreen")
    object LoginScreen:Destinations("LoginScreen")
    object MainScreen:Destinations("MainScreen")

    object ChatScreen:Destinations("ChatScreen")

}