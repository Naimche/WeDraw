package com.bupware.wedraw.android.roomData.tables.group

import kotlinx.coroutines.flow.Flow

class GroupRepository (private val groupDao: GroupDao) {

    suspend fun insert(group: Group) = groupDao.insert(group)

    suspend fun insertAll(groups: List<Group>) = groupDao.insertAll(groups)

    fun deleteAll() = groupDao.deleteAll()

    fun deleteGroup(groupId: Long) = groupDao.deleteGroupById(groupId)

    val readAllData : Flow<List<Group>> = groupDao.readAllData()

    fun getGroupByGroupId(groupId: Long): Flow<Group> = groupDao.getGroupByGroupId(groupId)



}