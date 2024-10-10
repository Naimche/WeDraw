package com.bupware.wedraw.android.roomData.tables.message

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessagesList(messages: List<Message>)
    @Query("SELECT * FROM messages_table ORDER BY id ASC")
    fun readAllDataMessage(): Flow<List<Message>>

    @Query("DELETE FROM messages_table WHERE id IS NULL AND date = :date")
    suspend fun deleteMessage(date: Date)
    @Query("SELECT * FROM messages_table WHERE owner_group_Id = :groupId")
    fun getMessagesByGroupId(groupId: Long): Flow<List<Message>>

    @Query("DELETE FROM messages_table")
    suspend fun deleteAll()

}

@Dao
interface MessageFailedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageFailed)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessagesList(messages: List<MessageFailed>)
    @Query("SELECT * FROM messagesFailed_table ORDER BY id ASC")
    fun readAllDataMessage(): Flow<List<MessageFailed>>

    @Query("DELETE FROM messagesFailed_table WHERE date = :date")
    suspend fun deleteMessage(date: Date)
    @Query("SELECT * FROM messagesFailed_table WHERE owner_group_Id = :groupId")
    fun getMessagesByGroupId(groupId: Long): Flow<List<MessageFailed>>

    @Query("DELETE FROM messagesFailed_table")
    suspend fun deleteAll()

}

@Dao
interface MessageWithImageFailedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageWithImageFailed)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessagesList(messages: List<MessageWithImageFailed>)
    @Query("SELECT * FROM messagesWithImageFailed_table ORDER BY id ASC")
    fun readAllDataMessage(): Flow<List<MessageWithImageFailed>>

    @Query("DELETE FROM messagesWithImageFailed_table WHERE date = :date")
    suspend fun deleteMessage(date: Date)
    @Query("SELECT * FROM messagesWithImageFailed_table WHERE owner_group_Id = :groupId")
    fun getMessagesByGroupId(groupId: Long): Flow<List<MessageWithImageFailed>>

    @Query("DELETE FROM messagesWithImageFailed_table")
    suspend fun deleteAll()

}

