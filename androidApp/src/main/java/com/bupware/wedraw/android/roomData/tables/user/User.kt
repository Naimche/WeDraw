package com.bupware.wedraw.android.roomData.tables.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User (
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val name: String,
    )