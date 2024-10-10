package com.bupware.wedraw.android.roomData.tables.group

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "groups_table")
data class Group(
    @PrimaryKey (autoGenerate = true)
    val groupId: Long = 0,
    val name: String,
    val code: String,
)
