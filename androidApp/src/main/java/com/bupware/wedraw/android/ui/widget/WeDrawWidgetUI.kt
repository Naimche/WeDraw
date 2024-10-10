package com.bupware.wedraw.android.ui.widget

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.bupware.wedraw.android.roomData.tables.message.Message
import com.bupware.wedraw.android.ui.widget.callback.WDrawReverseLetterCallback

@Composable
fun WeDrawWidgetUI(message: Message, bitmap: Bitmap) {

    WeDrawWidget(message, bitmap)

//                val uri = Uri.parse(state.data.image)
//                Log.i("ARM", "uri: $uri")
//                val inputStream = context.contentResolver.openInputStream(uri)
//                val bitmap = BitmapFactory.decodeStream(inputStream)

//                var width = bitmap.width
//                var height = bitmap.height
//                Log.i("ARM", "width: $width")
//                Log.i("ARM", "height: $height")

    // Resto del código que utiliza el objeto Uri


}

@Composable

private fun WeDrawWidget(message: Message, bitmap: Bitmap) {
    GlanceTheme {

        Column(
            modifier = GlanceModifier.fillMaxSize().background(ColorProvider(Color.White)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isreverse = currentState(WDrawReverseLetterCallback.IS_REVERSE) ?: false

            if (!isreverse) {
                Text(
                    message.text, modifier = GlanceModifier, style = TextStyle(
                        color = GlanceTheme.colors.textColorPrimary,
                        fontSize = 20.sp,
                    )
                )
                Box(modifier = GlanceModifier) {
                    Image(
                        provider = ImageProvider(
                            Bitmap.createScaledBitmap(
                                bitmap,
                                500,
                                500,
                                false
                            )
                        ),
                        contentDescription = "",
                        modifier = GlanceModifier.fillMaxSize()
                            .clickable(onClick = actionRunCallback(WDrawReverseLetterCallback::class.java))
                    )
//                        Button(text = "", onClick = actionRunCallback(WDrawReverseLetterCallback::class.java), modifier = GlanceModifier.size(200.dp, 200.dp).,)

                }

            } else {
                Box(
                    modifier = GlanceModifier
                        .background(ColorProvider(Color.LightGray))
                        .clickable(onClick = actionRunCallback(WDrawReverseLetterCallback::class.java))
                ) {
                    Column(modifier = GlanceModifier.fillMaxSize().padding(start = 10.dp, top = 20.dp)) {


                        Text(
                            message.text,
                            modifier =GlanceModifier,
                            style = TextStyle(
                                color = GlanceTheme.colors.onPrimary,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Start
                                )
                        )
                        Row(
                            horizontalAlignment = Alignment.End,
                            modifier = GlanceModifier.padding(top = 10.dp)
                        ) {
                            Text(
                                text = message.ownerId,

                                style = TextStyle(textAlign = TextAlign.Start,
                                    color = GlanceTheme.colors.onPrimary)
                            )
                        }

                    }


                }


            }


        }
    }

}