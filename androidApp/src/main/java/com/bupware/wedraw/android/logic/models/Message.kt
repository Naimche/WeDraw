package com.bupware.wedraw.android.logic.models

import androidx.compose.ui.graphics.ImageBitmap
import java.util.Date
import java.util.TimeZone

data class Message(
    var id:Long?,
    val text: String,
    var timeZone: TimeZone?,
    var senderId: String,
    var imageID: Long?,
    var groupId: Long,
    var date: Date?,
    var bitmap: ImageBitmap?
): java.io.Serializable

