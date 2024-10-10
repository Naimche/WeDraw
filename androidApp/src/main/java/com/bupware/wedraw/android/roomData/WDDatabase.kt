package com.bupware.wedraw.android.roomData

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bupware.wedraw.android.roomData.tables.message.Message
import com.bupware.wedraw.android.roomData.tables.message.MessageDao
import com.bupware.wedraw.android.roomData.tables.converter.DataConverter
import com.bupware.wedraw.android.roomData.tables.group.Group
import com.bupware.wedraw.android.roomData.tables.group.GroupDao
import com.bupware.wedraw.android.roomData.tables.image.Image
import com.bupware.wedraw.android.roomData.tables.image.ImageDao
import com.bupware.wedraw.android.roomData.tables.message.MessageFailed
import com.bupware.wedraw.android.roomData.tables.message.MessageFailedDao
import com.bupware.wedraw.android.roomData.tables.message.MessageWithImageFailed
import com.bupware.wedraw.android.roomData.tables.message.MessageWithImageFailedDao
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupUserCrossRef
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupWithUsersDao
import com.bupware.wedraw.android.roomData.tables.relationTables.messageWithImage.MessageWithImageDao
import com.bupware.wedraw.android.roomData.tables.user.User
import com.bupware.wedraw.android.roomData.tables.user.UserDao

@Database(entities = [User::class, Group::class, Image::class, Message::class, GroupUserCrossRef::class, MessageFailed::class, MessageWithImageFailed::class], version = 2)
@TypeConverters(DataConverter::class)
abstract class WDDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun imageDao(): ImageDao
    abstract fun messageDao(): MessageDao
    abstract fun messageFailedDao(): MessageFailedDao

    abstract fun messageWithImageFailedDao(): MessageWithImageFailedDao
    abstract fun groupWithUsersDao(): GroupWithUsersDao

    abstract fun messageWithImageDao(): MessageWithImageDao



    companion object {
        @Volatile
        private var INSTANCE: WDDatabase? = null

        fun getDatabase(context: Context): WDDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                // Create database here
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    WDDatabase::class.java,
                    "wedraw_database"
                //TODO: Remove fallbackToDestructiveMigration() el dia de despliegue
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}