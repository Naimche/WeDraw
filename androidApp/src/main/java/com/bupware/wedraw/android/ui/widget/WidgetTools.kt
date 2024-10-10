package com.bupware.wedraw.android.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import java.io.IOException

@Composable
fun BitmapImageWG(draw: Bitmap) {
    Column(modifier = GlanceModifier.fillMaxSize()) {
        Image(
            provider = ImageProvider(draw),
            contentDescription = null,
            modifier = GlanceModifier.fillMaxSize()
        )
    }

}

@Composable
fun convertImageBitmapToBitmap(bitmap: ImageBitmap): Bitmap {
    return bitmap.asAndroidBitmap()
}


fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        BitmapFactory.decodeStream(inputStream, null, options)
    } catch (e: IOException) {
        Log.i("WeDrawWidget", "getBitmapFromUri: IOException ${e.stackTraceToString()}")
        e.printStackTrace()
        null
    }
}


