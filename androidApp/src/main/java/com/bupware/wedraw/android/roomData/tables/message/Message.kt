package com.bupware.wedraw.android.roomData.tables.message

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bupware.wedraw.android.roomData.tables.group.Group
import com.bupware.wedraw.android.roomData.tables.user.User
import java.sql.Date


@Entity(
    tableName = "messages_table", foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["groupId"],
            childColumns = ["owner_group_Id"]
        ),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["ownerId"])
    ]
)

data class Message(
    @PrimaryKey(autoGenerate = false)
    val id: Long?,
    val owner_group_Id: Long,
    val ownerId: String,
    val text: String,
    val image_Id: Long? = null,
    val date: Date?
)

@Entity(
    tableName = "messagesFailed_table", foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["groupId"],
            childColumns = ["owner_group_Id"]
        ),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["ownerId"])
    ]
)
data class MessageFailed(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val owner_group_Id: Long,
    val ownerId: String,
    val text: String,
    val image_Id: Long? = null,
    val date: Date?,
    val uri: String?,
    val bitmap: ByteArray?
) {
    fun toMessage(): Message {

        return Message(
            id = this.id,
            owner_group_Id = this.owner_group_Id,
            ownerId = this.ownerId,
            text = this.text,
            image_Id = this.image_Id,
            date = this.date
        )
    }
}

@Entity(
    tableName = "messagesWithImageFailed_table", foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["groupId"],
            childColumns = ["owner_group_Id"]
        ),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["ownerId"])
    ]
)
data class MessageWithImageFailed(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val owner_group_Id: Long,
    val ownerId: String,
    val text: String,
    val image_Id: Long? = null,
    val date: Date?,
    val uri: String?,
    val bitmap: ByteArray?
) {
    fun toMessage(): Message {

        return Message(
            id = this.id,
            owner_group_Id = this.owner_group_Id,
            ownerId = this.ownerId,
            text = this.text,
            image_Id = this.image_Id,
            date = this.date
        )
    }
}



