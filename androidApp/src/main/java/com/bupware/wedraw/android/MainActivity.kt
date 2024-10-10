package com.bupware.wedraw.android

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.components.composables.SnackbarWrapper
import com.bupware.wedraw.android.components.extra.GetDeviceConfig
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.DataUtils
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.bupware.wedraw.android.logic.navigation.NavigationHost
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            FirebaseApp.initializeApp(this);

            MyApplicationTheme {

                GetDeviceConfig()

                Scaffold {
                    StartingPoint()

                    SnackbarWrapper()
                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun StartingPoint(){

    val context = LocalContext.current
    val dataUtils = DataUtils()
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        if (Firebase.auth.currentUser != null) {
            scope.launch {
                dataUtils.initData(context)
            }
        }

    }

    NavigationHost(navController = rememberNavController(), startDestination = Destinations.SplashScreen.ruta )
}


@HiltAndroidApp
class WeDraw: Application(){}



