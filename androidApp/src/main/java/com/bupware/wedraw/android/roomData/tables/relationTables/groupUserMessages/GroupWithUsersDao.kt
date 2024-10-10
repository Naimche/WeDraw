package com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupWithUsersDao  {

    //@Transaction
   // @Query("SELECT * FROM groups_table WHERE groupId = :groupId")
    //fun getGroupWithUsersByGroupId(groupId: Long): GroupWithUsers?

    @Transaction
    @Query("SELECT * FROM groups_table WHERE groupId = :groupId")
    fun getGroupWithUsersByGroupId(groupId: Long): GroupWithUsers?

    @Query("DELETE FROM GroupUserCrossRef")
    fun deleteAll()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupWithUser(groupWithUser :GroupUserCrossRef)

    @Query("SELECT * FROM GroupUserCrossRef WHERE groupId = :groupId")
    fun getSetOfUsersGroupByGroupID(groupId: Long): Flow<List<GroupUserCrossRef>>


    @Query("SELECT * FROM GroupUserCrossRef order by groupId ASC")
    fun readAllData(): Flow<List<GroupUserCrossRef>>
}

@Dao
interface UsersWithGroupDao {
    @Transaction
    @Query("SELECT * FROM users_table WHERE userId = :userId")
    fun getUserWithGroupsByUserId(userId: Long): UsersWithGroup?
}