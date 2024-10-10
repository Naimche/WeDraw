package com.bupware.wedraw.android.roomData.tables.image

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_table")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uri: String
)