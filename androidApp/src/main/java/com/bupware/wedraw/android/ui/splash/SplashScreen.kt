package com.bupware.wedraw.android.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.logic.dataHandler.DataUtils
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewSplash(){
    SplashScreenBody()
}

@Composable
fun SplashScreen(navController: NavController){

    SplashScreenBody()

    //NAVIGATE
    LaunchedEffect(true){
        if (Firebase.auth.currentUser != null){
            navController.navigate(route = Destinations.MainScreen.ruta)
        } else navController.navigate(route = Destinations.LoginScreen.ruta)
    }


}

@Composable
fun SplashScreenBody(){

    Column(
        Modifier
            .fillMaxSize()
            ) {

        Box(contentAlignment = Alignment.Center) {

            Image(
                painter = painterResource(R.drawable.mainbackground),
                contentDescription = "background",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "background",
                modifier = Modifier

            )

        }

    }
}