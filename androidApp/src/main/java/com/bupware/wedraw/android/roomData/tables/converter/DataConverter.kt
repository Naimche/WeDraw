package com.bupware.wedraw.android.roomData.tables.converter
import java.sql.Date;
import androidx.room.TypeConverter;
import com.bupware.wedraw.android.roomData.tables.group.Group
import com.bupware.wedraw.android.roomData.tables.user.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    private val gson = Gson()


    @TypeConverter
    fun fromTimeStamp(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromGroup(group: Group): String {
        return gson.toJson(group)
    }

    @TypeConverter
    fun toGroup(groupString: String): Group {
        return gson.fromJson(groupString, Group::class.java)
    }

    @TypeConverter
    fun fromUserList(userList: List<User>): String {
        val type = object : TypeToken<List<User>>() {}.type
        return gson.toJson(userList, type)
    }

    @TypeConverter
    fun toUserList(userListString: String): List<User> {
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(userListString, type)
    }
}
