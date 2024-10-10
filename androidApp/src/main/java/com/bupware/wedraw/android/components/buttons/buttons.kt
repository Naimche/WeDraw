package com.bupware.wedraw.android.components.buttons

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.composables.SnackbarManager
import com.bupware.wedraw.android.components.textfields.CreateGroupTextfield
import com.bupware.wedraw.android.components.textfields.JoinGroupTextfield
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.MemoryData
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.bupware.wedraw.android.theme.blueWeDraw
import com.bupware.wedraw.android.theme.Lexend
import com.bupware.wedraw.android.theme.blueVariant2WeDraw
import com.bupware.wedraw.android.theme.blueVariantWeDraw
import com.bupware.wedraw.android.theme.greenWeDraw
import com.bupware.wedraw.android.theme.redWeDraw
import com.bupware.wedraw.android.theme.yellowWeDraw
import com.bupware.wedraw.android.ui.chatScreen.ChatScreenViewModel
import com.bupware.wedraw.android.ui.mainscreen.MainViewModel
import io.ak1.drawbox.DrawController

//region MainScreen
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CreateGroupButton(viewModel: MainViewModel = hiltViewModel()){

    //val colors = listOf<Color>(blueWeDraw, greenWeDraw, yellowWeDraw, redWeDraw)
    val selectedColor = blueWeDraw

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(Modifier.clickable { viewModel.expandButton(1) }) {

            //region Shadow Color
            Column(
                Modifier
                    .background(selectedColor, RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.85f)

            ) {
                Text(text = " ")


                AnimatedContent(targetState = viewModel.expandCreateGroup, transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(300, easing = EaseOut),
                    ) with slideOutVertically(
                        animationSpec = tween(200, easing = EaseOut),
                    )
                }) { state ->


                    Column(
                        Modifier
                            .height(if (state) 320.dp else 50.dp)


                    ) {
                        Text(text = " ")
                    }


                }
            }

            //endregion
            Column(Modifier.fillMaxWidth(0.85f)) {

                val targetTextColor by animateColorAsState(
                    targetValue = if (viewModel.expandCreateGroup) Color.White else Color.Black,
                    animationSpec = tween(durationMillis = 500)
                )

                val targetColor by animateColorAsState(
                    targetValue = if (viewModel.expandCreateGroup) blueVariant2WeDraw else Color.White,
                    animationSpec = tween(durationMillis = 500)
                )

                //region UNIRSE A GRUPO LABEL
                Column(
                    Modifier
                        .height(60.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        Modifier
                            .background(
                                targetColor, RoundedCornerShape(10.dp)
                            )
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),) {
                        Text(
                            color = targetTextColor,
                            text = stringResource(R.string.crear_grupo),
                            fontSize = 20.sp,
                            fontFamily = Lexend,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
                //endregion

                //region CONTENT EXPANDIDO

                Column(
                    Modifier
                        .offset(y = (-10).dp)
                        .fillMaxWidth()
                        .background(
                            Color.White, RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)
                        )) {


                    AnimatedContent(
                        targetState = viewModel.expandCreateGroup,
                        transitionSpec = {
                            slideInVertically(
                                animationSpec = tween(300, easing = EaseOut),
                            ) with
                                    slideOutVertically(
                                        animationSpec = tween(200, easing = EaseOut),
                                    )
                        }
                    ) { state ->

                        Column(
                            Modifier
                                .height(if (state) 260.dp else 0.dp)
                                .fillMaxWidth()
                        ) {

                            Column(
                                Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(text = stringResource(R.string.nombre_del_grupo), fontSize = 20.sp,fontFamily = Lexend, fontWeight = FontWeight.Bold, color = Color.Black)
                                Spacer(modifier = Modifier.height(10.dp))
                                if (viewModel.expandCreateGroup) Column(Modifier.padding(start = 10.dp, end = 10.dp)){CreateGroupTextfield(value = viewModel.groupName, onValueChange = {viewModel.groupName = it})}
                                Spacer(modifier = Modifier.height(20.dp))
                                Column(Modifier.padding(start = 10.dp, end = 10.dp)) {
                                    if (viewModel.expandCreateGroup) PeopleLimitBar()
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                if (viewModel.expandCreateGroup) CreateGroupSubButton(viewModel::createGroupButton)
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                        }}
                }

                //endregion

            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun JoinGroupButton(viewModel: MainViewModel = hiltViewModel()){

    val selectedColor = redWeDraw
    
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(Modifier.clickable { viewModel.expandButton(2) }) {

            //region Shadow Color
            Column(
                Modifier
                    .background(selectedColor, RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.85f)
            ) {
                Text(text = " ")


                AnimatedContent(targetState = viewModel.expandJoinGroup, transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(300, easing = EaseOut),
                    ) with slideOutVertically(
                        animationSpec = tween(200, easing = EaseOut),
                    )
                }) { state ->


                    Column(
                        Modifier
                            .height(if (state) 260.dp else 50.dp)


                    ) {
                        Text(text = " ")
                    }


                }
            }

        //endregion
        Column(Modifier.fillMaxWidth(0.85f)) {

            val targetTextColor by animateColorAsState(
                targetValue = if (viewModel.expandJoinGroup) Color.White else Color.Black,
                animationSpec = tween(durationMillis = 500)
            )

            val targetColor by animateColorAsState(
                targetValue = if (viewModel.expandJoinGroup) blueVariant2WeDraw else Color.White,
                animationSpec = tween(durationMillis = 500)
            )

            //region UNIRSE A GRUPO LABEL
            Column(
                Modifier
                    .height(60.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    Modifier
                        .background(
                            targetColor, RoundedCornerShape(10.dp)
                        )
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),) {
                    Text(
                        color = targetTextColor,
                        text = stringResource(R.string.unirse_a_grupo),
                        fontSize = 20.sp,
                        fontFamily = Lexend,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
            //endregion

            //region CONTENT EXPANDIDO

            Column(
                Modifier
                    .offset(y = (-10).dp)
                    .fillMaxWidth()
                    .background(
                        Color.White, RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)
                    )) {


            AnimatedContent(
                targetState = viewModel.expandJoinGroup,
                transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(300, easing = EaseOut),
                    ) with
                            slideOutVertically(
                                animationSpec = tween(200, easing = EaseOut),
                            )
                }
            ) { state ->

                Column(
                    Modifier
                        .height(if (state) 200.dp else 0.dp)
                        .fillMaxWidth()
                        ) {

                    Column(
                        Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                        , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = stringResource(R.string.c_digo_del_grupo), fontSize = 20.sp,fontFamily = Lexend, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(10.dp))
                        if (viewModel.expandJoinGroup) Column(Modifier.padding(start = 10.dp, end = 10.dp)){JoinGroupTextfield(value = viewModel.joinCode, onValueChange = {viewModel.joinCode = it})}
                        Spacer(modifier = Modifier.height(20.dp))
                        if (viewModel.expandJoinGroup) JoinGroupSubButton(viewModel::joinGroupButton)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                }}
            }

            //endregion

        }
        }
    }
    
}

@Composable
fun PeopleLimitBar(){
    Row(
        Modifier
            .height(40.dp)
            .background(blueVariantWeDraw, RoundedCornerShape(10.dp))
            .padding(start = 10.dp, end = 10.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${stringResource(R.string.limite_de_personas)} 5", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = Lexend)
        Spacer(modifier = Modifier.weight(1f))

        Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
            PremiumInfoButton()
        }

    }
}

@Composable
fun PremiumInfoButton(){
    val snackbarText = stringResource(R.string.hazte_premium_para_tener_m_s_grupos)
    Box(Modifier
        .clickable {
            SnackbarManager.newSnackbar(snackbarText, yellowWeDraw)
        }
        .background(Color.White, RoundedCornerShape(10.dp))
        .fillMaxHeight()
        .padding(start = 10.dp, end = 10.dp)
            ,contentAlignment = Alignment.Center) {
        Text(text = "?", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
    }
}

@Composable
fun JoinGroupSubButton(action: (Context) -> Unit) {

    val context = LocalContext.current

    Box(
        Modifier
            .width(IntrinsicSize.Max)
            .clickable { action(context) }
    ) {

        Column(
            Modifier
                .background(Color(0xFFAC2525), RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .height(36.dp)
        ) {
            Text(text = " ")
        }

        SubButtonNoShadow(stringResource(R.string.unirse_a_grupo))
    }
}

@Composable
fun CreateGroupSubButton(action: (Context) -> Unit) {

    val context = LocalContext.current

    Box(Modifier
        .clickable { action(context) }
        .width(IntrinsicSize.Max)

    ) {

        Column(
            Modifier
                .background(Color(0xFFAC2525), RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .height(36.dp)
        ) {
            Text(text = " ")
        }

        SubButtonNoShadow(stringResource(R.string.crear_grupo))
    }
}

@Composable
fun SubButtonNoShadow(text:String){
    Column(
        Modifier
            .height(IntrinsicSize.Max)
            .width(IntrinsicSize.Max)
            .background(Color(0xFFF53333), RoundedCornerShape(15.dp))
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = Lexend)
    }
}

@Composable
fun JoinGroupQRButton(){
    //TODO meter QR button
}

@Composable
fun GroupBar(nombre: String, idGroup: String,navController: NavController) {

    val colors = listOf<Color>(blueWeDraw, greenWeDraw, yellowWeDraw, redWeDraw)
    val hashCode = nombre.hashCode()
    val selectedIndex = Math.abs(hashCode) % colors.size
    val selectedColor = colors[selectedIndex]

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        //Esta row es el color de abajo
        Box(Modifier.clickable{navController.navigate(route = "${Destinations.ChatScreen.ruta}/$idGroup") }

        ){

            Row(
                Modifier
                    .height(70.dp)
                    .fillMaxWidth(0.85f)
                    .background(selectedColor, RoundedCornerShape(10.dp))
            ) {
                Text(text = "")
            }

            Row(
                Modifier
                    .height(60.dp)
                    .fillMaxWidth(0.85f)
                    .background(Color.White, RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = nombre,
                    fontSize = 20.sp,
                    fontFamily = Lexend,
                    color = Color.Black
                )

                Column(Modifier.weight(1f)) {

                }

                if (MemoryData.notificationList[idGroup.toLong()] != 0.toLong() && MemoryData.notificationList[idGroup.toLong()] != null) {

                    Column(
                        Modifier
                            .background(Color.Red, RoundedCornerShape(10.dp))
                            .height(IntrinsicSize.Max)
                            .width(40.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier,
                            text = MemoryData.notificationList[idGroup.toLong()].toString(),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            fontFamily = Lexend
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(Modifier.padding(end = 10.dp)) {
                        Box(
                            Modifier
                                .background(selectedColor, RoundedCornerShape(10.dp))
                                .height(IntrinsicSize.Max)
                                .width(IntrinsicSize.Max), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier,
                                imageVector = ImageVector.vectorResource(id = R.drawable.notification),
                                tint = Color.White,
                                contentDescription = "People in group"
                            )
                        }
                    }
                }


            }
        }
    }

}
//endregion

//region chatscreen
@Composable
fun SendMessageButton(action: (String,Context) -> Unit, viewModel: ChatScreenViewModel = hiltViewModel()){

    val context = LocalContext.current

    Button(onClick = { action(viewModel.writingMessage,context) }, shape = CircleShape,
    colors = ButtonDefaults.buttonColors(
        backgroundColor = blueVariant2WeDraw
    ), modifier = Modifier.size(45.dp)) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = ImageVector.vectorResource(id = R.drawable.send),
            tint = Color.White,
            contentDescription = "send message"
        )
    }
}

@Composable
fun SwitchToDrawingButton(action: () -> Unit, drawingState:Boolean){
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

        Box(
            Modifier
                .height(75.dp)
                .width(90.dp)
                .padding(5.dp)
                .background(redWeDraw, RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center
        ) {

            Box(
                Modifier.background(
                    blueVariant2WeDraw,
                    RoundedCornerShape(10.dp)
                )
            ) {
                IconButton(
                    modifier = Modifier.padding(top = 3.dp, bottom = 3.dp, start = 6.dp, end = 6.dp),
                    onClick = {
                       action()
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = if (drawingState) R.drawable.chat else R.drawable.draw),
                        tint = Color.White,
                        contentDescription = "draw"
                    )
                }
            }

        }
    }
}

//endregion

//region CHAT
@Composable
fun CircleColoredButton(color: Color, controller: DrawController){
    Box(modifier = Modifier
        .background(color, CircleShape)
        .width(40.dp)
        .fillMaxHeight()
        .clip(CircleShape)
        .clickable {
            controller.changeColor(color)
        }
    )
}

@Composable
fun MoreColorsButton(viewModel: ChatScreenViewModel = hiltViewModel()){
    IconButton(modifier = Modifier
        .background(Color.White, CircleShape)
        .width(40.dp)
        .fillMaxHeight(),onClick = {
        viewModel.colorWheelShow = true
    }) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.palette),
            tint = blueVariant2WeDraw,
            contentDescription = "MoreColors"
        )
    }
}

@Composable
fun DrawButton(state: Boolean, action: () -> Unit){
    Column(modifier = Modifier
        .fillMaxHeight()
        .width(40.dp)
        .background(
            color = if (state) Color(0xFFFFCC4D) else Color.White,
            shape = RoundedCornerShape(5.dp)
        ).clickable {
        action()
    }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.pen),
                tint = if (state) Color.White else Color.Black,
                contentDescription = "Draw"
            )

    }

}

@Composable
fun EraseButton(state: Boolean, action: () -> Unit){
    Column(modifier = Modifier
        .fillMaxHeight()
        .width(40.dp)
        .background(
            color = if (state) Color(0xFFFFCC4D) else Color.White,
            shape = RoundedCornerShape(5.dp)
        ).clickable {
        action()
    }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.eraser),
                tint = if (state) Color.White else Color.Black,
                contentDescription = "Draw"
            )
    }

}

@Composable
fun SizeButtons(controller: DrawController,viewModel: ChatScreenViewModel = hiltViewModel()){
    Row(
        Modifier
            .width(130.dp)
            .background(Color.White, RoundedCornerShape(5.dp))
            .fillMaxHeight()
            .padding(5.dp), horizontalArrangement = Arrangement.Center) {

        SizeButton(id = 1, state = viewModel.sizeState == 1, controller = controller)
        Spacer(modifier = Modifier.width(5.dp))
        SizeButton(id = 2, state = viewModel.sizeState == 2, controller = controller)
        Spacer(modifier = Modifier.width(5.dp))
        SizeButton(id = 3, state = viewModel.sizeState == 3, controller = controller)
        
    }
}

@Composable 
fun SizeButton(controller: DrawController,id: Int, state:Boolean, viewModel: ChatScreenViewModel = hiltViewModel()){

    val size = when(id) {
        1 -> 10.dp
        2 -> 20.dp
        3 -> 30.dp
        else -> 0.dp
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(35.dp)
            .background(
                color = if (state) Color(0xFFFFCC4D) else Color.White,
                shape = RoundedCornerShape(5.dp)
            )
            .border(width = 1.dp, shape = RoundedCornerShape(5.dp), color = Color(0xFFFFCC4D))
            .clickable {
                viewModel.sizeState = id;
                when (id) {
                    1 -> controller.changeStrokeWidth(10f)
                    2 -> controller.changeStrokeWidth(30f)
                    3 -> controller.changeStrokeWidth(60f)
                }
            }
            .clip(RoundedCornerShape(5.dp))
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(size),
            imageVector = ImageVector.vectorResource(id = R.drawable.point),
            tint = if (state) Color.White else Color.Black,
            contentDescription = "Draw"
        )
    }

}

@Composable
fun UndoButton(action: () -> Unit){
    Column(modifier = Modifier
        .fillMaxHeight()
        .width(40.dp)
        .background(
            color = Color.White, shape = RoundedCornerShape(5.dp)
        )
        .clickable {
            action()
        }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.undo),
                tint = Color.Black,
                contentDescription = "Undo"
            )
    }

}

@Composable
fun RedoButton(action: () -> Unit){
    Column(modifier = Modifier
        .fillMaxHeight()
        .width(40.dp)
        .background(color = Color.White, shape = RoundedCornerShape(5.dp))
        .clickable { action() }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.redo),
                tint = Color.Black,
                contentDescription = "Redo"
            )

    }

}

//endregion

