package com.bupware.wedraw.android.ui.mainscreen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.animations.ChipPop
import com.bupware.wedraw.android.components.buttons.CreateGroupButton
import com.bupware.wedraw.android.components.buttons.GroupBar
import com.bupware.wedraw.android.components.buttons.JoinGroupButton
import com.bupware.wedraw.android.components.composables.ColorfulLines
import com.bupware.wedraw.android.components.systembar.SystemBarColor
import com.bupware.wedraw.android.components.textfields.TextFieldMessage
import com.bupware.wedraw.android.components.textfields.TextFieldUsername
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.MemoryData
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.bupware.wedraw.android.theme.Lexend
import com.bupware.wedraw.android.theme.blueVariant2WeDraw
import com.bupware.wedraw.android.theme.blueWeDraw
import com.bupware.wedraw.android.theme.greenWeDraw
import com.bupware.wedraw.android.theme.redWeDraw
import com.bupware.wedraw.android.theme.yellowWeDraw
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
@Preview
fun PreviewMain(){
    MainScreen(rememberNavController())
}

@Composable
fun MainScreen(navController: NavController,viewModel: MainViewModel = hiltViewModel()){

    val context = LocalContext.current

    //region Forzar update de grupos de internet
    if (MemoryData.forceGroupsUpdate.value){
        MemoryData.forceGroupsUpdate.value = false
        viewModel.groupList = emptyList<Group>().toMutableList()
        viewModel.groupList = MemoryData.groupList
        viewModel.showGroups = true
    }
    //endregion

    LaunchedEffect(Unit){
        viewModel.initValues(context)
    }

    BackHandler() {}

    SystemBarColor(color = Color(0xFF2C4560))

    MainScreenBody(navController = navController)

    AnimatedVisibility(visible = viewModel.showSettings,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }),
        exit =  slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth })
    ) {
        SettingsMenu(navController)
    }

    if (viewModel.askForUsername) UsernamePopUp()

    if (viewModel.navigateToChat) {viewModel.navigateToChat = false;navController.navigate(route = "${Destinations.ChatScreen.ruta}/${viewModel.targetNavigation}")}
}


@Composable
fun MainScreenBody(navController: NavController, viewModel: MainViewModel = hiltViewModel()){

    //region Image background
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Red)) {

        Image(painter = painterResource(R.drawable.mainbackground), contentDescription = "background", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
    }
    //endregion


    val moveOffset by animateFloatAsState(
        targetValue = if (viewModel.showSettings) 250f else 0f,
        animationSpec = tween(easing = FastOutSlowInEasing)
    )

    //Movable Component
    Column(Modifier.offset(x = moveOffset.dp)) {
        Box() {
            //Fondo de los backgrounds
            Box {
                GroupBackground(navController)
                UpperBackgroundContent(navController)
            }


            //Boton +
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                IconButton(
                    onClick = { viewModel.moreOptionsEnabled = !viewModel.moreOptionsEnabled },
                    modifier = Modifier
                        .size(70.dp)
                        .background(
                            color = blueWeDraw,
                            shape = CircleShape
                        )

                ) {
                    Icon(
                        imageVector = if (viewModel.moreOptionsEnabled) ImageVector.vectorResource(R.drawable.minus) else ImageVector.vectorResource(R.drawable.add),
                        contentDescription = null,
                        tint = Color.White,


                        )
                }

            }

            //Logo
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "background",
                    modifier = Modifier
                        .size(250.dp)
                        .offset(y = (-40).dp)
                )
            }
        }


    }

    //Settings & Notifications
    Row(Modifier.fillMaxWidth()) {

        //Settings
        IconButton(modifier = Modifier.padding(top = 10.dp, start = 15.dp),onClick = {
            viewModel.showSettings = !viewModel.showSettings
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                tint = Color.White,
                contentDescription = "Settings"
            )
        }

    }

    //ColorfulLines
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        ColorfulLines()
    }


}

@Composable
fun UsernamePopUp(viewModel: MainViewModel = hiltViewModel()){

    val context = LocalContext.current

    Box(contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xD3000000))) {
            Text(text = " ")
        }

        Column(
            Modifier
                .background(blueVariant2WeDraw, RoundedCornerShape(15.dp))
                .fillMaxHeight(0.3f)
                .fillMaxWidth(0.9f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.elige_un_nickname), fontFamily = Lexend, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Column(Modifier.padding(start = 10.dp, end = 10.dp)) {
                TextFieldMessage(viewModel.username,onValueChange = {viewModel.username = it}, placeholder = "Nickname")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { if (viewModel.username.isNotBlank()) viewModel.launchUpdateUsername(context) },colors = ButtonDefaults.buttonColors(backgroundColor = greenWeDraw)) {
                Text(text = stringResource(R.string.validar))
            }
        }

    }
}


//region Background && Content
@Composable
fun GroupBackground(navController: NavController,viewModel: MainViewModel = hiltViewModel()){
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 90.dp, bottom = 60.dp, start = 20.dp, end = 20.dp)
            .background(Color.Black.copy(0.4f), RoundedCornerShape(15.dp))) {
    }
}

@Composable
fun UpperBackgroundContent(navController: NavController,viewModel: MainViewModel = hiltViewModel()) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 90.dp, bottom = 60.dp)
    ) {
        when (viewModel.moreOptionsEnabled) {
            true -> SettingsContent()
            false -> GroupContent(navController = navController)
        }

    }
}
//endregion


//region Groups

@Composable
fun GroupContent(viewModel: MainViewModel = hiltViewModel(),navController: NavController){

    if (!viewModel.showGroups){

        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = blueVariant2WeDraw)
        }

    }
    else if (viewModel.showGroups && viewModel.groupList.isEmpty()) {

        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.crea_o_nete_a_un_grupo), fontFamily = Lexend, fontWeight = FontWeight.Bold)
        }

    }
    else {

        var animationLoading = true

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 70.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(viewModel.groupList.size) { index ->

                //region delay Incremental
                var visible by remember { mutableStateOf(false) }

                if (animationLoading) {
                    LaunchedEffect(viewModel.showGroups) {
                        delay((index + 1) * 100L) // Retraso incremental para cada elemento
                        visible = true
                        animationLoading = false
                    }
                } else visible = true
                //endregion


                Column(Modifier.padding(bottom = 25.dp)) {
                    ChipPop(
                        content = { GroupBar(viewModel.groupList.toList()[index].name, viewModel.groupList.toList()[index].id.toString() ,navController) },
                        show = visible
                    )
                }

            }
        }
    }
}

//endregion

//region Settings content
@Composable
fun SettingsContent(viewModel: MainViewModel = hiltViewModel()){

    var buttonDelay by remember { mutableStateOf(false) }
    var buttonDelay2 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        delay(100)
        buttonDelay = true
        delay(100)
        buttonDelay2 = true
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 70.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(Modifier.padding(bottom = 25.dp)) {
                ChipPop(content = { CreateGroupButton() }, show = buttonDelay)
            }
            Column(Modifier.padding(bottom = 25.dp)) {
                ChipPop(content = { JoinGroupButton() }, show = buttonDelay2)
            }

        }
    }

}
//endregion

@Composable
fun SettingsMenu(navController: NavController,viewModel: MainViewModel = hiltViewModel()){

    val interactionSource = remember { MutableInteractionSource() }

        Column() {
            Box(Modifier.fillMaxSize()) {

                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { viewModel.showSettings = !viewModel.showSettings })

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 90.dp, bottom = 60.dp, start = 0.dp, end = 160.dp)
                        .background(
                            Color.Black.copy(0.4f),
                            RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp)
                        ), verticalArrangement = Arrangement.Bottom) {

                    Box(modifier = Modifier.padding(bottom = 20.dp)) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {})

                        Column(
                            Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Row() {
                                Text(text = "WeDraw ", fontFamily = Lexend, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 25.sp)
                                Text(text = "B", fontFamily = Lexend, fontWeight = FontWeight.Bold, color = blueWeDraw, fontSize = 25.sp)
                                Text(text = "E", fontFamily = Lexend, fontWeight = FontWeight.Bold, color = greenWeDraw, fontSize = 25.sp)
                                Text(text = "T", fontFamily = Lexend, fontWeight = FontWeight.Bold, color = yellowWeDraw, fontSize = 25.sp)
                                Text(text = "A", fontFamily = Lexend, fontWeight = FontWeight.Bold, color = redWeDraw, fontSize = 25.sp)
                            }
                        }

                        Column(
                            Modifier
                                .padding(bottom = 20.dp)
                                .align(Alignment.BottomCenter)) {
                            LogOutButton(navController)
                        }
                    }
                }

            }
        }


}

@Composable
fun LogOutButton(navController: NavController){
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                Firebase.auth.signOut();

                navController.navigate(Destinations.LoginScreen.ruta) {
                    popUpTo(Destinations.MainScreen.ruta) {
                        inclusive = true
                    }
                }
            }
            .background(redWeDraw.copy(0.5f)), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.logout),
            contentDescription = null,
            tint = Color.White)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Logout", fontFamily = Lexend, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 25.sp)
    }
}

