package com.bupware.wedraw.android.roomData.tables.message

import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messageDao: MessageDao) {
    suspend fun insert(message: Message) {
        messageDao.insertMessage(message)
    }

    suspend fun insertMessagesList(messages: List<Message>) {
        messageDao.insertMessagesList(messages)
    }

    val readAllData: Flow<List<Message>> = messageDao.readAllDataMessage()

    suspend fun deleteAll() = messageDao.deleteAll()

    suspend fun deleteMessage(message: Message) = messageDao.deleteMessage(message.date!!)

    suspend fun getMessagesByGroupId(groupId: Long): Flow<List<Message>> = messageDao.getMessagesByGroupId(groupId)


}

class MessageFailedRepository(private val messageFailedDao: MessageFailedDao) {
    suspend fun insert(message: MessageFailed) {
        messageFailedDao.insertMessage(message)
    }

    suspend fun insertMessagesList(messages: List<MessageFailed>) {
        messageFailedDao.insertMessagesList(messages)
    }

    val readAllData: Flow<List<MessageFailed>> = messageFailedDao.readAllDataMessage()

    suspend fun deleteAll() = messageFailedDao.deleteAll()

    suspend fun deleteMessage(message: MessageFailed) = messageFailedDao.deleteMessage(message.date!!)

    suspend fun getMessagesByGroupId(groupId: Long): Flow<List<MessageFailed>> = messageFailedDao.getMessagesByGroupId(groupId)


}

class MessageWithImageFailedRepository(private val messageFailedDao: MessageWithImageFailedDao) {
    suspend fun insert(message: MessageWithImageFailed) {
        messageFailedDao.insertMessage(message)
    }

    suspend fun insertMessagesList(messages: List<MessageWithImageFailed>) {
        messageFailedDao.insertMessagesList(messages)
    }

    val readAllData: Flow<List<MessageWithImageFailed>> = messageFailedDao.readAllDataMessage()

    suspend fun deleteAll() = messageFailedDao.deleteAll()

    suspend fun deleteMessage(message: MessageWithImageFailed) = messageFailedDao.deleteMessage(message.date!!)

    suspend fun getMessagesByGroupId(groupId: Long): Flow<List<MessageWithImageFailed>> = messageFailedDao.getMessagesByGroupId(groupId)


}