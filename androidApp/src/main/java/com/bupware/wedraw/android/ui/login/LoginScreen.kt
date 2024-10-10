package com.bupware.wedraw.android.ui.login

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.DataUtils
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.bupware.wedraw.android.theme.Lexend
import com.bupware.wedraw.android.theme.blueVariant2WeDraw
import com.bupware.wedraw.android.theme.blueWeDraw
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

@Composable
@Preview
fun PreviewLogin() {
    LoginScreen(rememberNavController())
}

@Composable
fun LoginScreen(navController: NavController,viewModel: LoginViewModel = hiltViewModel() ) {

    val context = LocalContext.current

    BackHandler() {}


    if (viewModel.initNContinue) {
        LaunchedEffect(Unit) {

            viewModel.stopLoading = true
            viewModel.initNContinue = false

            val datautils = DataUtils()
            viewModel.viewModelScope.launch {

                datautils.initData(context).also {
                    viewModel.stopLoading = false
                    navController.navigate(route = Destinations.MainScreen.ruta) {
                        navController.popBackStack()
                    }
                }

            }
        }
    }

    LoginScreenBody(navController)

    if (viewModel.stopLoading) LoadingInit()
}

@Composable
fun LoginScreenBody(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {

    //Background
    Column(
        Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.mainbackground),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }


    Logo()
    Column(Modifier.padding(top = 230.dp, start = 10.dp, end = 10.dp)) {
        TransparentBackground(navController)
    }


}

@Composable
fun TransparentBackground(navController: NavController) {
    Column(
        Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(Color.Black.copy(0.4f), RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LogWithGoogle(navController = navController)

    }
}

@Composable
fun Logo() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "background",
            modifier = Modifier
                .size(300.dp)
                .offset(y = (-40).dp)

        )
    }
}

@Composable
fun LogWithGoogle(viewModel: LoginViewModel = hiltViewModel(), navController: NavController) {

    val context = LocalContext.current
    Log.i("Auth", "LogWithGoogle")
    val scope = rememberCoroutineScope()


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            viewModel.signInWithGoogleCredential(credential) {

                viewModel.initNContinue = true

            }


        } catch (e: Exception) {
            Log.e("Auth", "GoogleSignIn Failed!")
            Log.e("Auth", e.stackTraceToString())
        }


    }

    Button(
        modifier = Modifier
            .fillMaxWidth(0.90f)
            .height(60.dp)
        ,
        contentPadding = PaddingValues(2.dp),
        border = BorderStroke(4.dp,Color.White),
        colors = ButtonDefaults.buttonColors(backgroundColor = blueWeDraw),
        shape = RoundedCornerShape(15.dp),
        onClick = {

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.firebase_server_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, options)
        val signInIntent = googleSignInClient.signInIntent
        //googleSignInClient.signOut() // Agrega esta línea para asegurarte de que se muestre la pantalla de selección de cuenta cada vez
        launcher.launch(signInIntent)

    }) {

        Row(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp))
                , verticalAlignment = Alignment.CenterVertically) {

            Box(Modifier.weight(0.3f), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(R.drawable.googleicon), contentDescription = "Google Icon")
            }

            Box(Modifier.weight(1f)) {
                Text(text = stringResource(R.string.acceder_con_google), fontFamily = Lexend, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)

            }

        }
    }


}

@Composable
fun LoadingInit(){
    Box(contentAlignment = Alignment.Center) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5f))
            .clickable { })

        CircularProgressIndicator(color = blueVariant2WeDraw)
    }

}




