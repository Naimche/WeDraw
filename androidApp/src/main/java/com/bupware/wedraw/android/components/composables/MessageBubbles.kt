package com.bupware.wedraw.android.components.composables

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bupware.wedraw.android.components.extra.DeviceConfig
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.MemoryData
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.theme.blueVariant2WeDraw
import com.bupware.wedraw.android.ui.chatScreen.ChatScreenViewModel
import java.util.Date
import java.util.Calendar

@Composable
fun MessageBubbleHost(message: Message, showTriangle:Boolean, viewModel: ChatScreenViewModel = hiltViewModel()){
    val context = LocalContext.current

    val containsBitmap = message.bitmap != null
    val containsImageId = message.imageID != null

    val uriExist = viewModel.messageUrisList[message.imageID] != null

    val inputStream = if (containsImageId) context.contentResolver.openInputStream(viewModel.messageUrisList[message.imageID]!!) else null
    val bitmap: Bitmap? = if (inputStream != null) BitmapFactory.decodeStream(inputStream) else null

    val cornerShape = with(LocalDensity.current) { 16.dp.toPx() }
    val arrowWidth = with(LocalDensity.current) { if (showTriangle) 8.dp.toPx() else 0.dp.toPx() }
    val arrowHeight = with(LocalDensity.current) {if (showTriangle) 12.dp.toPx() else 0.dp.toPx()  }

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .wrapContentSize()
            .padding(end = 16.dp, start = 30.dp),
        horizontalAlignment = Alignment.End
    ) {

        Surface(elevation = 3.dp, shape =
        if (showTriangle){
            RightBubbleShape(
                cornerShape = cornerShape,
                arrowWidth = arrowWidth,
                arrowHeight = arrowHeight,
                arrow = false
            )} else RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .drawRightBubble(
                        arrow = false,
                        cornerShape = cornerShape,
                        arrowWidth = arrowWidth,
                        arrowHeight = arrowHeight,
                        bubbleColor = Color(0xFFB7FF91)
                    )
            ) {

                when{

                    containsImageId && uriExist -> {
                        MessageHostBodyImageID(bitmap = bitmap!!, message = message,showTriangle)
                    }

                    containsBitmap -> {
                        MessageHostBodyBitmap(message = message,showTriangle)
                    }

                    else -> MessageHostBodyText(message)
                }

            }
        }
    }
}

@Composable
fun MessageHostBodyImageID(bitmap: Bitmap, message: Message,showTriangle:Boolean){
    Column(Modifier.padding(5.dp)) {
        if (showTriangle) {
            Text(
                text = MemoryData.userList.first { it.id == message.senderId }.username.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = blueVariant2WeDraw
                , textAlign = TextAlign.Start
            )
        }
        Box() {
            Image(modifier = Modifier
                .clip(RoundedCornerShape(15.dp)),
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "")
            Column(Modifier.fillMaxSize(),verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
                Text(
                    text = convertirHoraYMinutos(message.date!!),
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 4.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                    fontSize = 14.sp,
                    color = Color(0xFF444444)
                )
            }
        }
    }
}

@Composable
fun MessageHostBodyBitmap(message: Message,showTriangle:Boolean){
    Column(Modifier.padding(5.dp)) {
        if (showTriangle) {
            Text(
                text = MemoryData.userList.first { it.id == message.senderId }.username.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = blueVariant2WeDraw
                , textAlign = TextAlign.Start
            )
        }
        Box() {
            Image(modifier = Modifier
                .clip(RoundedCornerShape(15.dp)),
                bitmap = message.bitmap!! ,
                contentDescription = "")
            Column(Modifier.fillMaxSize(),verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
                Text(
                    text = convertirHoraYMinutos(message.date!!),
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 4.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                    fontSize = 14.sp,
                    color = Color(0xFF444444)
                )
            }
        }
    }
}

@Composable
fun MessageHostBodyText(message: Message){
    Row(Modifier, verticalAlignment = Alignment.Bottom) {

        Column(Modifier) {
            Text(
                text = message.text,
                modifier = Modifier
                    .widthIn(max = DeviceConfig.widthPercentage(75))
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
                fontSize = 14.sp,
                color = Color.Black
            )
        }


        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            // Hora
            Text(
                text = convertirHoraYMinutos(message.date!!),
                modifier = Modifier.padding(
                    top = 8.dp,
                    start = 4.dp,
                    end = 8.dp,
                    bottom = 8.dp
                ),
                fontSize = 14.sp,
                color = Color(0xFF444444)
            )
        }

    }
}

fun convertirHoraYMinutos(date: Date): String {

    val calendar = Calendar.getInstance()
    calendar.time = date

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return String.format("%02d:%02d", hour, minute)
}

@Composable
fun MessageBubble(message: Message, showTriangle:Boolean, viewModel: ChatScreenViewModel = hiltViewModel()){

    val context = LocalContext.current

    val containsBitmap = message.bitmap != null
    val containsImageId = message.imageID != null

    val uriExist = viewModel.messageUrisList[message.imageID] != null

    val inputStream = if (containsImageId && uriExist) context.contentResolver.openInputStream(viewModel.messageUrisList[message.imageID]!!) else null
    val bitmap: Bitmap? = if (inputStream != null) BitmapFactory.decodeStream(inputStream) else null

    val cornerShape = with(LocalDensity.current) { 16.dp.toPx() }
    val arrowWidth = with(LocalDensity.current) { if (showTriangle) 8.dp.toPx() else 0.dp.toPx() }
    val arrowHeight = with(LocalDensity.current) {if (showTriangle) 12.dp.toPx() else 0.dp.toPx()  }

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .wrapContentSize()
            .graphicsLayer(rotationY = 180f)
            .padding(end = 16.dp, start = 30.dp),
        horizontalAlignment = Alignment.End
    ) {

        Surface(elevation = 3.dp, shape =
        if (showTriangle){
            RightBubbleShape(
                cornerShape = cornerShape,
                arrowWidth = arrowWidth,
                arrowHeight = arrowHeight,
                arrow = false
            )} else RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .drawRightBubble(
                        cornerShape = cornerShape,
                        arrowWidth = arrowWidth,
                        arrowHeight = arrowHeight,
                        bubbleColor = Color.White
                    )
            ) {

                when{

                    containsImageId && uriExist -> {
                        MessageBodyImageID(bitmap = bitmap!!, message = message,showTriangle)
                    }

                    containsBitmap -> {
                        MessageBodyImageBitmap(message = message,showTriangle)
                    }

                    else -> MessageBodyText(showTriangle,message)
                }


            }
        }
    }
}

@Composable
fun MessageBodyImageID(bitmap: Bitmap,message: Message,showTriangle:Boolean){

    Column(Modifier.padding(5.dp).graphicsLayer(rotationY = 180f)) {
        if (showTriangle) {
            Text(
                text = MemoryData.userList.first { it.id == message.senderId }.username.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = blueVariant2WeDraw
                , textAlign = TextAlign.Start
            )
        }
        Box() {
            Image(modifier = Modifier.clip(RoundedCornerShape(15.dp)),bitmap = bitmap!!.asImageBitmap(), contentDescription = "")
            Column(Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = convertirHoraYMinutos(message.date!!),
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 4.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                    fontSize = 14.sp,
                    color = Color(0xFF444444)
                )
            }
        }
    }

}

@Composable
fun MessageBodyImageBitmap(message: Message,showTriangle:Boolean){
    Column(Modifier.padding(5.dp).graphicsLayer(rotationY = 180f)) {
        if (showTriangle) {
            Text(
                text = MemoryData.userList.first { it.id == message.senderId }.username.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = blueVariant2WeDraw
                , textAlign = TextAlign.Start
            )
        }
        Box() {
            Image(modifier = Modifier.clip(RoundedCornerShape(15.dp)),bitmap = message.bitmap!!, contentDescription = "")
            Column(Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = convertirHoraYMinutos(message.date!!),
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 4.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                    fontSize = 14.sp,
                    color = Color(0xFF444444)
                )
            }
        }
    }
}

@Composable
fun MessageBodyText(showTriangle:Boolean,message: Message){

    Column() {

        if (showTriangle) {
            Text(
                text = MemoryData.userList.first { it.id == message.senderId }.username.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(rotationY = 180f)
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 0.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = blueVariant2WeDraw
                , textAlign = TextAlign.Start
            )
        }

        Row(Modifier, verticalAlignment = Alignment.Bottom) {

            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                // Hora
                Text(
                    text = convertirHoraYMinutos(message.date!!),
                    modifier = Modifier
                        .padding(
                            top = 0.dp,
                            start = 8.dp,
                            end = 4.dp,
                            bottom = 8.dp
                        )
                        .graphicsLayer(rotationY = 180f),
                    fontSize = 14.sp,
                    color = Color(0xFF444444)
                )
            }

            Column(Modifier, horizontalAlignment = Alignment.Start) {
                Text(
                    text = message.text,
                    modifier = Modifier
                        .widthIn(max = DeviceConfig.widthPercentage(75))
                        .padding(
                            top = 8.dp,
                            start = 4.dp,
                            end = 8.dp,
                            bottom = 8.dp
                        )
                        .graphicsLayer(rotationY = 180f),
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }


        }
    }
}