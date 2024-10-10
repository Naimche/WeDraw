package com.bupware.wedraw.android.logic.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bupware.wedraw.android.ui.login.LoginScreen
import com.bupware.wedraw.android.ui.chatScreen.ChatScreen
import com.bupware.wedraw.android.ui.drawingScreen.DrawingScreen
import com.bupware.wedraw.android.ui.mainscreen.MainScreen
import com.bupware.wedraw.android.ui.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavigationHost (navController: NavHostController,startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    )
    {
        composable(Destinations.SplashScreen.ruta){
            SplashScreen(navController)
        }

        composable(Destinations.DrawingScreen.ruta) {
            DrawingScreen(navController = navController)
        }
        
        composable(Destinations.LoginScreen.ruta){
            LoginScreen(navController = navController)
        }
        composable(Destinations.MainScreen.ruta){
            MainScreen(navController = navController)
        }


        composable("${Destinations.ChatScreen.ruta}/{groupId}", arguments = listOf(navArgument("groupId"){type = NavType.LongType})){ backStackEntry ->
            backStackEntry.arguments?.getLong("groupId")
                ?.let {
                    ChatScreen(navController =navController, groupId = it)
                }
        }
    }
}









