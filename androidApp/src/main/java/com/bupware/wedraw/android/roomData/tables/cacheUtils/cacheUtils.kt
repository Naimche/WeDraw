package com.bupware.wedraw.android.roomData.tables.cacheUtils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

interface cacheUtils {

    fun saveImageToCache(context: Context, bitmap: Bitmap, filename: String): Uri? {
        val cacheDirectory = context.cacheDir
        val imageFile = File(cacheDirectory, filename)

        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Obtén el URI del archivo de imagen en la caché

        return FileProvider.getUriForFile(
            context,
            "com.bupware.wedraw.android.fileprovider",
            imageFile
        )
    }

}