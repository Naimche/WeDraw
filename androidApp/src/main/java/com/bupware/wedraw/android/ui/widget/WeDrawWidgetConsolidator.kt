package com.bupware.wedraw.android.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.image.Image
import com.bupware.wedraw.android.roomData.tables.message.Message
import com.bupware.wedraw.android.roomData.tables.relationTables.messageWithImage.MessageWithImage
import com.bupware.wedraw.android.roomData.tables.relationTables.messageWithImage.MessageWithImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeDrawWidgetConsolidator : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    companion object {
        private val SMALL_BOX = DpSize(90.dp, 90.dp)
        private val BIG_BOX = DpSize(180.dp, 180.dp)
        private val VERY_BIG_BOX = DpSize(300.dp, 300.dp)
        private val ROW = DpSize(180.dp, 48.dp)
        private val LARGE_ROW = DpSize(300.dp, 48.dp)
        private val COLUMN = DpSize(48.dp, 180.dp)
        private val LARGE_COLUMN = DpSize(48.dp, 300.dp)
    }

    override val sizeMode: SizeMode
        get() = SizeMode.Responsive(
            setOf(
                SMALL_BOX,
                BIG_BOX,
                VERY_BIG_BOX,
                ROW,
                LARGE_ROW,
                COLUMN,
                LARGE_COLUMN
            )
        )

    @SuppressLint("CoroutineCreationDuringComposition")
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val dataStore = WeDrawPreferences(context)
        provideContent {
            val localcontext = LocalContext.current

            val glanceId = LocalGlanceId.current
            var uri = currentState(stringPreferencesKey(WeDrawPreferences.WIMAGE_KEY))
            //pruebas
            val scope = rememberCoroutineScope()
            var message = MessageWithImage(
                image = Image(id = 0, uri = "null"),
                message = Message(
                    id = 0,
                    text = "error",
                    ownerId = "s",
                    owner_group_Id = 0,
                    date = null
                )
            )
            scope.launch(Dispatchers.IO) {


                val messageWithImageDao = WDDatabase.getDatabase(context).messageWithImageDao()
                val repository: MessageWithImageRepository =
                    MessageWithImageRepository(messageWithImageDao)

                message = repository.getMessageWithImage(1)
                Log.i("ARM", "message: ${message.image.uri}")

                //Todo: cambiar por un callback
                updateAppWidgetState(localcontext, glanceId) { pref ->
                    pref[stringPreferencesKey(WeDrawPreferences.WIMAGE_KEY)] = message.image.uri
                    WeDrawWidgetConsolidator().update(localcontext, glanceId)
                }

            }

            var bitmap: Bitmap? = null


            if (uri == "wimage_key" || uri == null) {
                GlanceTheme {
                    Column(modifier = GlanceModifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                val uriParse = Uri.parse(uri)
                Log.i("ARM", "uri: $uri")
                val inputStream = context.contentResolver.openInputStream(uriParse)
                bitmap = BitmapFactory.decodeStream(inputStream)

                GlanceTheme {
                    Log.i("ARM", "bitmap: $bitmap")
                    if (bitmap != null) {
                        val size = LocalSize.current
                        when (size) {
                            SMALL_BOX -> {
                                Text(text = "SMALL_BOX")
                            }

                            BIG_BOX -> {
                                Text(
                                    text = "BIG_BOX",
                                    style = TextStyle(color = GlanceTheme.colors.errorContainer)
                                )
                                if (message != null) {
                                    WeDrawWidgetUI(message = message.message, bitmap = bitmap)
                                }
                            }

                            VERY_BIG_BOX -> {
                                Text(
                                    text = "VERY_BIG_BOX",
                                    style = TextStyle(color = GlanceTheme.colors.errorContainer)
                                )
                            }

                            ROW -> {
                                Text(
                                    text = "ROW",
                                    style = TextStyle(color = GlanceTheme.colors.errorContainer)
                                )
                            }

                            LARGE_ROW -> {
                                Text(
                                    text = "LARGE_ROW",
                                    style = TextStyle(color = GlanceTheme.colors.errorContainer)
                                )
                            }

                            COLUMN -> {
                                Text(
                                    text = "COLUMN",
                                    style = TextStyle(color = GlanceTheme.colors.errorContainer)
                                )
                            }

                            LARGE_COLUMN -> {
                                Text(
                                    text = "LARGE_COLUMN",
                                    style = TextStyle(color = GlanceTheme.colors.errorContainer)
                                )
                            }
                        }

                    }

                }
            }


        }


    }
}


