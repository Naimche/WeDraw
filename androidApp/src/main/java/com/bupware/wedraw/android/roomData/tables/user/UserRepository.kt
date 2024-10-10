package com.bupware.wedraw.android.roomData.tables.user

import com.bupware.wedraw.android.roomData.tables.group.Group
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    suspend fun insertAll(users: List<User>) = userDao.insertAll(users)

    val readAllData : Flow<List<User>> = userDao.readAllData()

    fun getUserByID(userID : String) : Flow<User> = userDao.getUserByID(userID)

}