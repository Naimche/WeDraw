package com.bupware.wedraw.android.roomData.tables.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bupware.wedraw.android.roomData.tables.group.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users_table ORDER BY userId ASC")
    fun readAllData(): Flow<List<User>>


    @Query("SELECT * FROM users_table WHERE userId = :userID")
    fun getUserByID(userID : String) : Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users : List<User>):  List<Long>
}