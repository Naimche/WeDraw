package com.bupware.wedraw.android.roomData.tables.relationTables.messageWithImage

import androidx.room.Embedded
import androidx.room.Relation
import com.bupware.wedraw.android.roomData.tables.image.Image
import com.bupware.wedraw.android.roomData.tables.message.Message

data class MessageWithImage(
    @Embedded val image: Image,
    @Relation(
        parentColumn = "id",
        entityColumn = "image_Id"
    )
    val message: Message
)




