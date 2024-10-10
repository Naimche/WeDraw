package com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages

import kotlinx.coroutines.flow.Flow

class GroupWithUsersRepository(private val groupWithUsersDao: GroupWithUsersDao)
{
    suspend fun insert(groupUserCrossRef: GroupUserCrossRef) {
        groupWithUsersDao.insertGroupWithUser(groupUserCrossRef)
    }

    fun getAllUserCrossRefByGroupID(groupID : Long) : Flow<List<GroupUserCrossRef>> = groupWithUsersDao.getSetOfUsersGroupByGroupID(groupID)

    val readAllData : Flow<List<GroupUserCrossRef>> = groupWithUsersDao.readAllData()




}